package com.mcintyret.jvm.core.opcode.math.mul;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IMul extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a * b;
    }

    @Override
    public byte getByte() {
        return 0x68;
    }

}
