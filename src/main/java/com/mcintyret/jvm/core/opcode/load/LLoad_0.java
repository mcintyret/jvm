package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LLoad_0 extends DoubleWidthLoad_0 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x1E;
    }
}
