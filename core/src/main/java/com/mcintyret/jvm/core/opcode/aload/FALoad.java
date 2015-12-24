package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FALoad extends SingleWidthALoad implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x30;
    }
}
