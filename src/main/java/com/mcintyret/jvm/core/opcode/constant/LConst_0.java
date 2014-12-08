package com.mcintyret.jvm.core.opcode.constant;

class LConst_0 extends LConst {

    @Override
    protected long getConst() {
        return 0L;
    }

    @Override
    public byte getByte() {
        return 0x09;
    }
}
