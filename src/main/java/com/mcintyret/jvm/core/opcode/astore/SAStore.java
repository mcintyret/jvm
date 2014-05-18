package com.mcintyret.jvm.core.opcode.astore;

class SAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x56;
    }
}
