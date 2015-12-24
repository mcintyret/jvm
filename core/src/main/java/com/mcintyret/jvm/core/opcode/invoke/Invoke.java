package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.exec.Execution;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.opcode.AThrow;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

abstract class Invoke extends OpCode {

    private static final Logger LOG = LoggerFactory.getLogger(Invoke.class);

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShortUnsigned());
        LOG.info("Invoking {}.{}", method.getClassObject().getClassName(), method.getSignature());

        Variables args = getMethodArgs(ctx, method);
        doInvoke(method, args, ctx);
    }

    protected abstract void doInvoke(Method method, Variables args, OperationContext ctx);

    protected void invokeNativeMethod(NativeMethod nativeMethod, Variables args, OperationContext ctx) {
        Execution nativeExecution = new Execution(nativeMethod, args, ctx.getThread());

        NativeImplementation nativeImplementation = nativeMethod.getNativeImplementation();
        if (nativeImplementation == null) {
            throw new IllegalStateException("No Native implementation for " + nativeMethod.getClassObject().getClassName() + "." + nativeMethod.getSignature());
        }
        NativeReturn nr = nativeImplementation.execute(args, nativeExecution);
        nativeExecution.onComplete(); // Releases lock if this method was synchronized

        nr.applyValue(ctx.getStack());
        if (nr.isThrowable()) {
            new AThrow().execute(ctx);
        }
    }

    private Variables getMethodArgs(OperationContext ctx, Method method) {
        boolean isStatic = method.isStatic();

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
    public boolean isSafePoint() {
        return true;
    }


}
