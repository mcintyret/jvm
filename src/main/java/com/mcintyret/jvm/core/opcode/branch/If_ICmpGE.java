package com.mcintyret.jvm.core.opcode.branch;

class If_ICmpGE extends IntBinaryCondition {

    @Override
    protected boolean conditionMet(int a, int b) {
        return a <= b;
    }

    @Override
    public byte getByte() {
        return (byte) 0xA2;
    }
}
