package com.mcintyret.jvm.core.opcode.logic.xor;

import com.mcintyret.jvm.core.opcode.BinaryLongOp;

class LXor extends BinaryLongOp {

    @Override
    protected long binaryOp(long a, long b) {
        return a ^ b;
    }

    @Override
    public byte getByte() {
        return (byte) 0x83;
    }
}
