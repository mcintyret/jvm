package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.CharTyped;

class CALoad extends SingleWidthALoad implements CharTyped {

    @Override
    public byte getByte() {
        return 0x34;
    }
}
