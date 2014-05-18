package com.mcintyret.jvm.core.opcode.math.add;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LAdd extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a + b;
    }

    @Override
    public byte getByte() {
        return 0x61;
    }
}
