package com.mcintyret.jvm.core.opcode.invoke;

class InvokeSpecial extends Invoke {

    @Override
    public byte getByte() {
        return (byte) 0xB7;
    }

}