package com.mcintyret.jvm.core.opcode.math.div;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IDiv extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return b / a;
    }

    @Override
    public byte getByte() {
        return 0x6C;
    }
}
