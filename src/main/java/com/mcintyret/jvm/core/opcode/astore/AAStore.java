package com.mcintyret.jvm.core.opcode.astore;

class AAStore extends SingleWidthAStore {

    @Override
    public byte getByte() {
        return 0x53;
    }
}
