package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LLoad_1 extends DoubleWidthLoad_1 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x1F;
    }
}
