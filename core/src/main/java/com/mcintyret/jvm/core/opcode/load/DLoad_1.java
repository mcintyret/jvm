package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DLoad_1 extends DoubleWidthLoad_1 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x27;
    }
}
