package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IAStore extends SingleWidthAStore implements IntTyped {

    @Override
    public byte getByte() {
        return 0x4F;
    }
}
