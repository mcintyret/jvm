package com.mcintyret.jvm.core.opcode.aload;

class LALoad extends DoubleWidthALoad {

    @Override
    public byte getByte() {
        return 0x2F;
    }
}
