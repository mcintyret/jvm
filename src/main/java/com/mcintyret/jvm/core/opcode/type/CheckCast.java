package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.WordStack;

class CheckCast extends TypeOp {

    @Override
    protected void handleType(boolean instanceOf, WordStack stack) {
        if (!instanceOf) {
            throw new ClassCastException();
        }
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
