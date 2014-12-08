package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LStore extends DoubleWidthStoreIndexed implements LongTyped {

    @Override
    public byte getByte() {
        return 0x37;
    }
}
