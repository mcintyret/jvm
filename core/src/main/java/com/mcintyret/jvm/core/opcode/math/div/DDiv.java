package com.mcintyret.jvm.core.opcode.math.div;

import com.mcintyret.jvm.core.opcode.BinaryDoubleOp;

class DDiv extends BinaryDoubleOp {

    @Override
    protected double binaryOp(double a, double b) {
        return b / a;
    }

    @Override
    public byte getByte() {
        return 0x6F;
    }
}
