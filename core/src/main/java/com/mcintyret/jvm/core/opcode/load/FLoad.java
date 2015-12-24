package com.mcintyret.jvm.core.opcode.load;

class FLoad extends SingleWidthLoadIndexed {

    @Override
    public byte getByte() {
        return 0x17;
    }
}
