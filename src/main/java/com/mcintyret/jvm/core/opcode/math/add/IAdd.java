package com.mcintyret.jvm.core.opcode.math.add;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IAdd extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a + b;
    }

    @Override
    public byte getByte() {
        return 0x60;
    }

}
