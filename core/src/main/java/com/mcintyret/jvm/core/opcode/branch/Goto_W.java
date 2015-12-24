package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
class Goto_W extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getByteIterator().seek(ctx.getByteIterator().nextInt() - 5);
    }

    @Override
    public byte getByte() {
        return (byte) 0xC8;
    }
}