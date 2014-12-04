package com.mcintyret.jvm.core.opcode.math;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
class IInc extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ByteIterator bi = ctx.getByteIterator();
        ctx.getLocalVars()[bi.nextByte()] += bi.nextByte();
    }

    @Override
    public byte getByte() {
        return (byte) 0x84;
    }
}
