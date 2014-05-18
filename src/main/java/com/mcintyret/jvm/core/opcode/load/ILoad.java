package com.mcintyret.jvm.core.opcode.load;

class ILoad extends SingleWidthLoadIndexed {

    @Override
    public byte getByte() {
        return 0X15;
    }
}
