package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FStore extends SingleWidthStoreIndexed implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x38;
    }
}
