package com.mcintyret.jvm.core.opcode.invoke;

import com.mcintyret.jvm.core.clazz.InterfaceMethod;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.oop.OopClass;

class InvokeInterface extends InvokeIndirect {

    @Override
    public byte getByte() {
        return (byte) 0xB9;
    }

    @Override
    protected Method getImplementationMethod(Method method, OopClass oop) {
        return ((InterfaceMethod) method).getMethodForImplementation(oop.getClassObject());
    }
}
