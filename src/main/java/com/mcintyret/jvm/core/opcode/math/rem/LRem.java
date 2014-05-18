package com.mcintyret.jvm.core.opcode.math.rem;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LRem extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a % b;
    }

    @Override
    public byte getByte() {
        return 0x71;
    }
}
