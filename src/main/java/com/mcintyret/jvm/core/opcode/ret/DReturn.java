package com.mcintyret.jvm.core.opcode.ret;

class DReturn extends DoubleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAF;
    }
}
