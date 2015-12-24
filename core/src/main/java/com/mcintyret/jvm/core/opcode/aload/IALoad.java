package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IALoad extends SingleWidthALoad implements IntTyped {

    @Override
    public byte getByte() {
        return 0x2E;
    }
}
