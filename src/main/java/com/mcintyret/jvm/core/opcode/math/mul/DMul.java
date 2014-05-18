package com.mcintyret.jvm.core.opcode.math.mul;

import com.mcintyret.jvm.core.opcode.BinaryDoubleOp;

class DMul extends BinaryDoubleOp {


    @Override
    public byte getByte() {
        return 0x6B;
    }

    @Override
    protected double binaryOp(double a, double b) {
        return a * b;
    }
}
