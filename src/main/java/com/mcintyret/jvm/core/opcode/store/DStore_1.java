package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DStore_1 extends DoubleWidthStore_1 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x48;
    }
}
