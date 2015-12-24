package com.mcintyret.jvm.core.opcode.load;

class ALoad extends SingleWidthLoadIndexed {

    @Override
    public byte getByte() {
        return 0x19;
    }
}
