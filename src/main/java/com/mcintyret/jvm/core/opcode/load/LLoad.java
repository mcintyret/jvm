package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LLoad extends DoubleWidthLoadIndexed implements LongTyped {

    @Override
    public byte getByte() {
        return 0x16;
    }
}
