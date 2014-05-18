package com.mcintyret.jvm.core.opcode.store;

class IStore extends SingleWidthStoreIndexed {

    @Override
    public byte getByte() {
        return 0X36;
    }
}
