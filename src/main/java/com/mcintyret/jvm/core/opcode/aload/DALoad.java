package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DALoad extends DoubleWidthALoad implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x31;
    }
}
