package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.Heap;

class IfNull extends RefUnaryCondition {

    @Override
    protected boolean conditionMet(int pop) {
        return pop == Heap.NULL_POINTER;
    }

    @Override
    public byte getByte() {
        return (byte) 0xC6;
    }
}
