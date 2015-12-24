package com.mcintyret.jvm.core.opcode.math.sub;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class ISub extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return b - a;
    }

    @Override
    public byte getByte() {
        return 0x64;
    }

}
