package com.mcintyret.jvm.core.opcode.condition;

class IfGE extends UnaryCondition {

    @Override
    protected boolean conditionMet(int pop) {
        return pop >= 0;
    }

    @Override
    public byte getByte() {
        return (byte) 0x9C;
    }
}
