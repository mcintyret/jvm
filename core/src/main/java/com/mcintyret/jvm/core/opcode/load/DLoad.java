package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.DoubleTyped;

class DLoad extends DoubleWidthLoadIndexed implements DoubleTyped {

    @Override
    public byte getByte() {
        return 0x18;
    }
}
