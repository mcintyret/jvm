package com.mcintyret.jvm.core.opcode.math.mul;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LMul extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a * b;
    }

    @Override
    public byte getByte() {
        return 0x69;
    }
}
