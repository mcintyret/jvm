package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.Heap;

class IfNonNull extends UnaryCondition {

    @Override
    protected boolean conditionMet(int pop) {
        return pop != Heap.NULL_POINTER;
    }

    @Override
    public byte getByte() {
        return (byte) 0xC7;
    }
}
