package com.mcintyret.jvm.core.opcode.logic.and;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IAnd extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a & b;
    }

    @Override
    public byte getByte() {
        return 0x7E;
    }
}
