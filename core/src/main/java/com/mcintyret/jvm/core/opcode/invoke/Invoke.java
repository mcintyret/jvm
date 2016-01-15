package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.exec.Execution;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.AThrow;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.parse.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

abstract class Invoke extends OpCode {

    private static final Logger LOG = LoggerFactory.getLogger(Invoke.class);

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShortUnsigned());
        boolean isStatic = method.isStatic();

        // Because the method signature is the same regardless of the implementation, we can work out the args before
        // we work out the actual method to use
        Variables args = getMethodArgs(ctx, method, isStatic);

        Oop oop = isStatic ? null : args.getOop(0);

        method = getImplementationMethod(method, oop);

        boolean isNative = method.hasModifier(Modifier.NATIVE);

        if (LOG.isInfoEnabled()) {
            LOG.info("{}Invoking {}.{}{}", makeSpace(ctx), method.getClassObject().getClassName(), method.getSignature(), isNative ? "        NATIVE" : "");
        }

        if (isNative) {
            invokeNativeMethod((NativeMethod) method, args, ctx);
        } else {
            // Because the implementation may have changed
            int maxLocalVars = method.getCode().getMaxLocals();
            if (maxLocalVars > args.length()) {
                args = args.copy(maxLocalVars);
            }
            ctx.getExecutionStack().push(
                new Execution(method, args, ctx.getThread()));
        }

        afterInvoke(ctx);
    }

    private String makeSpace(OperationContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ctx.getThread().getTotalStackDepth(); i++) {
            sb.append("  ");
        }
        return sb.toString();
    }


    protected Method getImplementationMethod(Method method, Oop oop) {
        return method;
    }


    protected void afterInvoke(OperationContext ctx) {
        // Do nothing by default
    }


    private void invokeNativeMethod(NativeMethod nativeMethod, Variables args, OperationContext ctx) {
        Execution nativeExecution = new Execution(nativeMethod, args, ctx.getThread());

        NativeImplementation nativeImplementation = nativeMethod.getNativeImplementation();
        if (nativeImplementation == null) {
            throw new IllegalStateException("No Native implementation for " + nativeMethod.getClassObject().getClassName() + "." + nativeMethod.getSignature());
        }

        Heap.enterNativeMethod();
        nativeExecution.getExecutionStack().push(nativeExecution);

        NativeReturn nr = nativeImplementation.execute(args, nativeExecution);

        nativeExecution.onComplete(); // Releases lock if this method was synchronized
        Heap.exitNativeMethod();
        nativeExecution.getExecutionStack().pop();

        LOG.debug("Returning from {}.{}", nativeMethod.getClassObject().getClassName(), nativeMethod.getSignature());
        nr.applyValue(ctx.getStack());
        if (nr.isThrowable()) {
            new AThrow().execute(ctx);
        }
    }

    private Variables getMethodArgs(OperationContext ctx, Method method, boolean isStatic) {
        int shift = isStatic ? 0 : 1;

        Variables args = method.newArgArray();

        List<Type> argTypes = method.getSignature().getArgTypes();
        int argCount = argTypes.size();
        ListIterator<Type> it = argTypes.listIterator(argCount);

        int pos = method.getSignature().getTotalWidth();
        while (it.hasPrevious()) {
            Type argType = it.previous();
            SimpleType simpleType = argType.asSimpleType();
            pos -= argType.getWidth();

            // +1 because 0 is this if non-static
            // TODO: this needs to be better
            if (argType.isDoubleWidth()) {
                args.putWide(pos + shift, simpleType, ctx.getStack().popDoubleWidth(simpleType));
            } else {
                args.put(pos + shift, simpleType, ctx.getStack().popSingleWidth(simpleType));
            }
        }

        if (!isStatic) {
            args.putOop(0, ctx.getStack().popOop());
        }

        return args;
    }


    // Method invocations are a safe point to stop and do GC
    @Override
    public final boolean isSafePoint() {
        return true;
    }
}
