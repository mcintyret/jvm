package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LALoad extends DoubleWidthALoad implements LongTyped {

    @Override
    public byte getByte() {
        return 0x2F;
    }
}
