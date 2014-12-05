package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.exec.VariableStack;

class CheckCast extends TypeOp {

    @Override
    protected void handleType(boolean instanceOf, VariableStack stack, int address) {
        if (!instanceOf) {
            throw new ClassCastException();
        }
        stack.push(address);
    }

    @Override
    protected void handleNull(VariableStack stack, int address) {
        stack.push(address);
    }

    @Override
    public byte getByte() {
        return (byte) 0xC0;
    }
}
