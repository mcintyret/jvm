package com.mcintyret.jvm.core.opcode.store;

class FStore extends SingleWidthStoreIndexed {

    @Override
    public byte getByte() {
        return 0x38;
    }
}
