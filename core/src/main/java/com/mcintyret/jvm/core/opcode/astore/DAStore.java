package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DAStore extends DoubleWidthAStore implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x52;
    }
}
