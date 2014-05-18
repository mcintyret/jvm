package com.mcintyret.jvm.core.opcode.math.sub;

import com.mcintyret.jvm.core.opcode.BinaryDoubleOp;

class DSub extends BinaryDoubleOp {

    @Override
    protected double binaryOp(double a, double b) {
        return b - a;
    }

    @Override
    public byte getByte() {
        return 0x67;
    }
}
