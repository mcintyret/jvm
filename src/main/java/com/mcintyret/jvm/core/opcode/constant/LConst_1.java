package com.mcintyret.jvm.core.opcode.constant;

class LConst_1 extends LConst {

    @Override
    protected long getConst() {
        return 1L;
    }

    @Override
    public byte getByte() {
        return 0x0A;
    }
}
