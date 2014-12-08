package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LAStore extends DoubleWidthAStore implements LongTyped {

    @Override
    public byte getByte() {
        return 0x50;
    }
}
