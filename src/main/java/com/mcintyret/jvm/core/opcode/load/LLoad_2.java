package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LLoad_2 extends DoubleWidthLoad_2 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x20;
    }
}
