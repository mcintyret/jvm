package com.mcintyret.jvm.core.opcode.logic.shift;

import com.mcintyret.jvm.core.opcode.BinaryIntOp;

public class IUShR extends BinaryIntOp {

    @Override
    protected int binaryOp(int a, int b) {
        return a >>> b;
    }

    @Override
    public byte getByte() {
        return 0x7C;
    }
}
