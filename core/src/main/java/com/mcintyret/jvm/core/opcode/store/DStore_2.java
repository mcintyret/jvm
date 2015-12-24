package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DStore_2 extends DoubleWidthStore_2 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x49;
    }
}
