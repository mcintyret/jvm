package com.mcintyret.jvm.core.opcode.constant;

class DConst_1 extends DConst {

    @Override
    protected double getConst() {
        return 1D;
    }

    @Override
    public byte getByte() {
        return 0x0F;
    }
}
