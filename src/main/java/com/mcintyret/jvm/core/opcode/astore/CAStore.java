package com.mcintyret.jvm.core.opcode.astore;

class CAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x55;
    }
}
