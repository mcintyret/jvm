package com.mcintyret.jvm.core.opcode.constant;

class IConst_1 extends IConst {

    @Override
    protected int getConst() {
        return 1;
    }

    @Override
    public byte getByte() {
        return 0x04;
    }
}

