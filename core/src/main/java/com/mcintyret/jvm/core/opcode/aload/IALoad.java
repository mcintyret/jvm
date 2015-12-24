package com.mcintyret.jvm.core.opcode.aload;

class IALoad extends SingleWidthALoad {

    @Override
    public byte getByte() {
        return 0x2E;
    }
}
