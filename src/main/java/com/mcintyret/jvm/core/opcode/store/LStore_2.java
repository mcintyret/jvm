package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LStore_2 extends DoubleWidthStore_2 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x41;
    }
}
