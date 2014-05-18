package com.mcintyret.jvm.core.opcode.aload;

class FALoad extends SingleWidthALoad {

    @Override
    public byte getByte() {
        return 0x30;
    }
}
