package com.mcintyret.jvm.core.opcode.math.rem;

import com.mcintyret.jvm.core.opcode.BinaryFloatOp;

class FRem extends BinaryFloatOp {

    @Override
    protected float binaryOp(float a, float b) {
        return a % b;
    }

    @Override
    public byte getByte() {
        return 0x72;
    }

}
