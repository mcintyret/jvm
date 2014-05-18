package com.mcintyret.jvm.core.opcode.ret;

class IReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAC;
    }
}
