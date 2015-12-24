package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

public class DLoad_3 extends DoubleWidthLoad_3 implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x29;
    }
}
