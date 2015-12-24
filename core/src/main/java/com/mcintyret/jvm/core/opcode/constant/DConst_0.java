package com.mcintyret.jvm.core.opcode.constant;

class DConst_0 extends DConst {

    @Override
    protected double getConst() {
        return 0D;
    }

    @Override
    public byte getByte() {
        return 0x0E;
    }
}
