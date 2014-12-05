package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.CharTyped;

class CAStore extends SingleWidthAStore implements CharTyped {

    @Override
    public byte getByte() {
        return 0x55;
    }
}
