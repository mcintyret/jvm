package com.mcintyret.jvm.core.opcode.astore;

class LAStore extends DoubleWidthAStore {

    @Override
    public byte getByte() {
        return 0x50;
    }
}
