package com.mcintyret.jvm.core.opcode.astore;

class DAStore extends DoubleWidthAStore {

    @Override
    public byte getByte() {
        return 0x52;
    }
}
