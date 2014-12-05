package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LReturn extends DoubleWidthReturn implements LongTyped {

    @Override
    public byte getByte() {
        return (byte) 0xAD;
    }
}
