package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.exec.VariableStack;

class InstanceOf extends TypeOp {

    @Override
    public byte getByte() {
        return (byte) 0xC1;
    }

    @Override
    protected void handleType(boolean instanceOf, VariableStack stack, int address) {
        stack.push(instanceOf ? 1 : 0);
    }

    @Override
    protected void handleNull(VariableStack stack, int address) {
        stack.push(0);
    }
}
