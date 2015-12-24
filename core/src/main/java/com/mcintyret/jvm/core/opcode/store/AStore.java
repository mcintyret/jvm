package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AStore extends SingleWidthStoreIndexed implements RefTyped {

    @Override
    public byte getByte() {
        return 0x3A;
    }

}
