package com.mcintyret.jvm.core.opcode.astore;

class IAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x4F;
    }
}
