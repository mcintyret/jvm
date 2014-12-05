package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DLoad_0 extends DoubleWidthLoad_0 implements DoubleTyped

    {

    @Override
    public byte getByte() {
        return 0x26;
    }
}
