package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.opcode.AThrow;
import com.mcintyret.jvm.core.opcode.OpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Invoke extends OpCode {

    private static final Logger LOG = LoggerFactory.getLogger(Invoke.class);

    @Override
    public final void execute(OperationContext ctx) {
        Method method = ctx.getConstantPool().getMethod(ctx.getByteIterator().nextShortUnsigned());
        LOG.info("Invoking {}.{}", method.getClassObject().getClassName(), method.getSignature());

        doInvoke(method, ctx);
    }

    protected abstract void doInvoke(Method method, OperationContext ctx);

    protected void invokeNativeMethod(NativeMethod nativeMethod, int[] args, OperationContext ctx) {
        NativeImplementation nativeImplementation = nativeMethod.getNativeImplementation();
        if (nativeImplementation == null) {
            throw new IllegalStateException("No Native implementation for " + nativeMethod.getClassObject().getClassName() + "." + nativeMethod.getSignature());
        }
        NativeReturn nr = nativeImplementation.execute(args, ctx);
        nr.applyValue(ctx.getStack());
        if (nr.isThrowable()) {
            new AThrow().execute(ctx);
        }
    }

    // Method invocations are a safe point to stop and do GC
    @Override
    public boolean isSafePoint() {
        return true;
    }

}
