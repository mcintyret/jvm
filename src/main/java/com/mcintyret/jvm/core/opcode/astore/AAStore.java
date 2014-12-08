package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AAStore extends SingleWidthAStore implements RefTyped {

    @Override
    public byte getByte() {
        return 0x53;
    }
}
