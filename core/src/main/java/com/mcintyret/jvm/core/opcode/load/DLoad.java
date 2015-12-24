package com.mcintyret.jvm.core.opcode.load;

class DLoad extends DoubleWidthLoadIndexed {

    @Override
    public byte getByte() {
        return 0x18;
    }
}
