package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.WordStack;

class InstanceOf extends TypeOp {

    @Override
    public byte getByte() {
        return (byte) 0xC1;
    }

    @Override
    protected void handleType(boolean instanceOf, WordStack stack, int address) {
        stack.push(instanceOf ? 1 : 0);
    }

    @Override
    protected void handleNull(WordStack stack) {
        stack.push(0);
    }
}
