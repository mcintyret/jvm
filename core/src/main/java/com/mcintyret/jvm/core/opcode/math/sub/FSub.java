package com.mcintyret.jvm.core.opcode.math.sub;

import com.mcintyret.jvm.core.opcode.BinaryFloatOp;

class FSub extends BinaryFloatOp {

    @Override
    protected float binaryOp(float a, float b) {
        return b - a;
    }

    @Override
    public byte getByte() {
        return 0x66;
    }

}
