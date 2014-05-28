package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.parse.attribute.CodeException;
import com.mcintyret.jvm.parse.attribute.CodeExceptions;

import java.util.List;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
class AThrow extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        OopClass thrown = Heap.getOopClass(ctx.getStack().pop());

        ExecutionStackElement elem = ctx.getExecutionStack().peek();
        while (elem != null) {
            Method m = elem.getMethod();
            ConstantPool cp = m.getClassObject().getConstantPool();
            List<CodeException> exceptions = m.getCode().getCodeExceptions();
            ByteIterator bi = elem.getByteIterator();
            int pos = bi.getPos();

            for (CodeException exception : exceptions) {
                if (pos >= exception.getStartPc() && pos <= exception.getEndPc()) {
                    AbstractClassObject caughtType = cp.getClassObject(exception.getCatchType());
                    if (thrown.getClassObject().isInstanceOf(caughtType)) {
                        // Actually caught it!

                        bi.setPos(exception.getHandlerPc());

                        elem.getStack().clear();
                        elem.getStack().push(thrown.getAddress());
                        return;
                    }
                }
            }

            ctx.getExecutionStack().pop();
            elem = ctx.getExecutionStack().peek();
        }

        // If we're here, the Exception has gone all the way to the top.
        ctx.getExecutionStack().setFinalReturn(NativeReturn.forInt(thrown.getAddress()));
    }


    @Override
    public byte getByte() {
        return (byte) 0xBF;
    }
}
