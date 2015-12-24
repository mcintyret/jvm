package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.LongTyped;

class LLoad_3 extends DoubleWidthLoad_3 implements LongTyped {

    @Override
    public byte getByte() {
        return 0x21;
    }
}
