package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LStore_1 extends DoubleWidthStore_1 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x40;
    }
}
