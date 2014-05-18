package com.mcintyret.jvm.core.opcode.math.div;

import com.mcintyret.jvm.core.opcode.BinaryFloatOp;

class FDiv extends BinaryFloatOp {

    @Override
    protected float binaryOp(float a, float b) {
        return a / b;
    }

    @Override
    public byte getByte() {
        return 0x6E;
    }
}
