package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.ByteTyped;

class BAStore extends SingleWidthAStore implements ByteTyped {

    @Override
    public byte getByte() {
        return 0x54;
    }
}
