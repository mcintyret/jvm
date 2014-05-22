package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.WordStack;

class CheckCast extends TypeOp {

    @Override
    protected void handleType(boolean instanceOf, WordStack stack, int address) {
        if (!instanceOf) {
            throw new ClassCastException();
        }
        stack.push(address);
    }

    @Override
    protected void handleNull(WordStack stack) {
        // Do nothing
    }

    @Override
    public byte getByte() {
        return (byte) 0xC0;
    }
}
