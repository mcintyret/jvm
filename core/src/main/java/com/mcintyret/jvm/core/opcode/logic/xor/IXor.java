package com.mcintyret.jvm.core.opcode.logic.xor;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

class IXor extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a ^ b;
    }

    @Override
    public byte getByte() {
        return (byte) 0x82;
    }
}
