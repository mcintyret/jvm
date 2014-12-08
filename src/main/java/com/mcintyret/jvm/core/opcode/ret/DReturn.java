package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DReturn extends DoubleWidthReturn implements DoubleTyped {

    @Override
    public byte getByte() {
        return (byte) 0xAF;
    }
}
