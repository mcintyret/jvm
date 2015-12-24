package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LStore_3 extends DoubleWidthStore_3 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x42;
    }
}
