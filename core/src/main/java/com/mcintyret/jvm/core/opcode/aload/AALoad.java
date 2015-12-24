package com.mcintyret.jvm.core.opcode.aload;

class AALoad extends SingleWidthALoad {

    @Override
    public byte getByte() {
        return 0x32;
    }

}
