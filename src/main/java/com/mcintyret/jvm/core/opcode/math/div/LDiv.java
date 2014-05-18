package com.mcintyret.jvm.core.opcode.math.div;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LDiv extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a / b;
    }

    @Override
    public byte getByte() {
        return 0x6D;
    }
}
