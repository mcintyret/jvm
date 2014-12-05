package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DStore_3 extends DoubleWidthStore_3 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x4A;
    }
}
