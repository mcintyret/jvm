package com.mcintyret.jvm.core.opcode.cmp;

class FCmpL extends FCmp {
    
    @Override
    protected int nanResult() {
        return -1;
    }

    @Override
    public byte getByte() {
        return (byte) 0x95;
    }
}
