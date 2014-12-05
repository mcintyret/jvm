package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DLoad_2 extends DoubleWidthLoad_2 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x28;
    }
}
