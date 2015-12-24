package com.mcintyret.jvm.core.opcode.astore;

class FAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x51;
    }
}
