package com.mcintyret.jvm.core.opcode.ret;

class AReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xB0;
    }
}
