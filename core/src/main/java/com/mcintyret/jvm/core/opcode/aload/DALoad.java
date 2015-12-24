package com.mcintyret.jvm.core.opcode.aload;

class DALoad extends DoubleWidthALoad {

    @Override
    public byte getByte() {
        return 0x31;
    }
}
