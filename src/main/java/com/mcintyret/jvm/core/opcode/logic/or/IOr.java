package com.mcintyret.jvm.core.opcode.logic.or;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IOr extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a | b;
    }

    @Override
    public byte getByte() {
        return (byte) 0x80;
    }
}
