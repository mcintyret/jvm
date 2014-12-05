package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LStore_0 extends DoubleWidthStore_0 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x3F;
    }
}
