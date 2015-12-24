package com.mcintyret.jvm.core.opcode.logic.and;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LAnd extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a & b;
    }

    @Override
    public byte getByte() {
        return 0x7F;
    }
}
