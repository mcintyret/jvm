package com.mcintyret.jvm.core.opcode.math.add;

import com.mcintyret.jvm.core.opcode.BinaryDoubleOp;

class DAdd extends BinaryDoubleOp {

    @Override
    protected double binaryOp(double a, double b) {
        return a + b;
    }

    @Override
    public byte getByte() {
        return 0x63;
    }
}
