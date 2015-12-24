package com.mcintyret.jvm.core.opcode.ret;

class FReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAE;
    }
}
