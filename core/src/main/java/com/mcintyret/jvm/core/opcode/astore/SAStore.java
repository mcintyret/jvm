package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.ShortTyped;

class SAStore extends SingleWidthAStore implements ShortTyped {

    @Override
    public byte getByte() {
        return 0x56;
    }
}
