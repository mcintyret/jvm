package com.mcintyret.jvm.core.opcode.math.rem;

import com.mcintyret.jvm.core.opcode.BinaryDoubleOp;

class DRem extends BinaryDoubleOp {

    @Override
    protected double binaryOp(double a, double b) {
        return b % a;
    }

    @Override
    public byte getByte() {
        return 0x73;
    }
}
