package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IStore extends SingleWidthStoreIndexed implements IntTyped {

    @Override
    public byte getByte() {
        return 0X36;
    }
}
