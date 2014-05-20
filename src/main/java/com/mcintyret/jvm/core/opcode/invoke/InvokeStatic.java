package com.mcintyret.jvm.core.opcode.invoke;

class InvokeStatic extends Invoke {

    @Override
    public byte getByte() {
        return (byte) 0xB8;
    }

    @Override
    protected boolean isStatic() {
        return true;
    }
}
