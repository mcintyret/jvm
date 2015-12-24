package com.mcintyret.jvm.core.opcode.store;

class LStore extends DoubleWidthStoreIndexed {

    @Override
    public byte getByte() {
        return 0x37;
    }
}
