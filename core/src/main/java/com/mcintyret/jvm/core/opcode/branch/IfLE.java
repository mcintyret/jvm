package com.mcintyret.jvm.core.opcode.branch;

class IfLE extends IntUnaryCondition {

    @Override
    protected boolean conditionMet(int pop) {
        return pop <= 0;
    }

    @Override
    public byte getByte() {
        return (byte) 0x9E;
    }
}
