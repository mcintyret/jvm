package com.mcintyret.jvm.core.opcode.cmp;

class DCmpG extends DCmp {

    @Override
    protected int nanResult() {
        return 1;
    }

    @Override
    public byte getByte() {
        return (byte) 0x98;
    }
}
