package com.mcintyret.jvm.core.opcode.astore;

class BAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x54;
    }
}
