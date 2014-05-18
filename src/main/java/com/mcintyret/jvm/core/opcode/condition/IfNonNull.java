package com.mcintyret.jvm.core.opcode.condition;

import com.mcintyret.jvm.core.Heap;

public class IfNonNull extends UnaryCondition {

    @Override
    protected boolean conditionMet(int pop) {
        return pop != Heap.NULL_POINTER;
    }

    @Override
    public byte getByte() {
        return (byte) 0xC7;
    }
}
