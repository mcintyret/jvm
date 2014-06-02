package com.mcintyret.jvm.core.opcode.invoke;


import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.oop.OopClass;

class InvokeVirtual extends InvokeIndirect {

    @Override
    public byte getByte() {
        return (byte) 0xB6;
    }

    @Override
    protected Method getImplementationMethod(Method method, OopClass oop) {
        Method ret = method;
        if (oop.getClassObject() != method.getClassObject()) {
            ret = oop.getClassObject().getInstanceMethods()[method.getOffset()];
        }
        return ret;
    }
}
