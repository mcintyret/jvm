package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AReturn extends SingleWidthReturn implements RefTyped {

    @Override
    public byte getByte() {
        return (byte) 0xB0;
    }
}
