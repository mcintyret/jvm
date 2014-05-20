package com.mcintyret.jvm.core.opcode.invoke;


class InvokeVirtual extends Invoke {

    @Override
    public byte getByte() {
        return (byte) 0xB6;
    }
}
