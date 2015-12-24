package com.mcintyret.jvm.core.opcode.aload;

class BALoad extends SingleWidthALoad {

    @Override
    public byte getByte() {
        return 0x33;
    }
}
