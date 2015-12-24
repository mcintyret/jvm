package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;

class Nop extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        // NoOp!
    }

    @Override
    public byte getByte() {
        return 0x00;
    }
}
