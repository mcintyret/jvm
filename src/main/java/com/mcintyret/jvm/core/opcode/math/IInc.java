package com.mcintyret.jvm.core.opcode.math;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
class IInc extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ByteIterator bi = ctx.getByteIterator();
        ctx.getVariables()[bi.next()] += bi.next();
    }

    @Override
    public byte getByte() {
        return (byte) 0x84;
    }
}
