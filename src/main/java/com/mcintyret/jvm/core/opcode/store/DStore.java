package com.mcintyret.jvm.core.opcode.store;

class DStore extends DoubleWidthStoreIndexed {

    @Override
    public byte getByte() {
        return 0x39;
    }
}
