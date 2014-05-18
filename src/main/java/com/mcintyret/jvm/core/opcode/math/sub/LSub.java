package com.mcintyret.jvm.core.opcode.math.sub;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LSub extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return b - a;
    }

    @Override
    public byte getByte() {
        return 0x65;
    }
}
