package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DStore extends DoubleWidthStoreIndexed implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x39;
    }
}
