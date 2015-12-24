package com.mcintyret.jvm.core.opcode.invoke;

class InvokeStatic extends InvokeSimple {

    @Override
    public byte getByte() {
        return (byte) 0xB8;
    }

}
