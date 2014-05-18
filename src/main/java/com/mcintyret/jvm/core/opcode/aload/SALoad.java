package com.mcintyret.jvm.core.opcode.aload;

class SALoad extends SingleWidthALoad {

    @Override
    public byte getByte() {
        return 0x35;
    }
}
