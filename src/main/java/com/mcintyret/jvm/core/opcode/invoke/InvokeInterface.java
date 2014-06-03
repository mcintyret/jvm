package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.InterfaceMethod;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OperationContext;

class InvokeInterface extends InvokeIndirect {

    @Override
    public byte getByte() {
        return (byte) 0xB9;
    }

    @Override
    protected Method getImplementationMethod(Method method, Oop oop) {
        return ((InterfaceMethod) method).getMethodForImplementation(((OopClass) oop).getClassObject());
    }

    @Override
    protected final void afterInvoke(OperationContext ctx) {
        ctx.getByteIterator().nextShort(); // InvokeInterface has 2 extra args which can be ignored
    }
}
