package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.ShortTyped;

class SALoad extends SingleWidthALoad implements ShortTyped {

    @Override
    public byte getByte() {
        return 0x35;
    }
}
