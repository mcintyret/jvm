package com.mcintyret.jvm.core.opcode.logic.or;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LOr extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a | b;
    }

    @Override
    public byte getByte() {
        return (byte) 0x81;
    }
}
