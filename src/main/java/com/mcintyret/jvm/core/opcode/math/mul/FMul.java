package com.mcintyret.jvm.core.opcode.math.mul;

import com.mcintyret.jvm.core.opcode.BinaryFloatOp;

class FMul extends BinaryFloatOp {

    @Override
    protected float binaryOp(float a, float b) {
        return a * b;
    }

    @Override
    public byte getByte() {
        return 0x6A;
    }
}
