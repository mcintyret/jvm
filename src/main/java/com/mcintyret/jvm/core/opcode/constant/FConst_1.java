package com.mcintyret.jvm.core.opcode.constant;

class FConst_1 extends FConst {

    @Override
    protected float getConst() {
        return 1F;
    }

    @Override
    public byte getByte() {
        return 0x0C;
    }
}
