package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.ByteTyped;

class BALoad extends SingleWidthALoad implements ByteTyped {

    @Override
    public byte getByte() {
        return 0x33;
    }
}
