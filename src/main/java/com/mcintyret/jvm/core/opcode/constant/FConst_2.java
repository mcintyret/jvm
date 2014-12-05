package com.mcintyret.jvm.core.opcode.constant;

class FConst_2 extends FConst {

    @Override
    protected float getConst() {
        return 2F;
    }

    @Override
    public byte getByte() {
        return 0x0D;
    }
}
