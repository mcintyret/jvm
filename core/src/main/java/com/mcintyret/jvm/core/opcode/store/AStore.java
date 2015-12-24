package com.mcintyret.jvm.core.opcode.store;

class AStore extends SingleWidthStoreIndexed {

    @Override
    public byte getByte() {
        return 0x3A;
    }
}
