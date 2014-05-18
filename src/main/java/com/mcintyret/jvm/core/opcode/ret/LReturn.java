package com.mcintyret.jvm.core.opcode.ret;

class LReturn extends DoubleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAD;
    }
}
