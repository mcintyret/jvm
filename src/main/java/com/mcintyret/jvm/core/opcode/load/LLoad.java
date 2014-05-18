package com.mcintyret.jvm.core.opcode.load;

class LLoad extends DoubleWidthLoadIndexed {

    @Override
    public byte getByte() {
        return 0x16;
    }
}
