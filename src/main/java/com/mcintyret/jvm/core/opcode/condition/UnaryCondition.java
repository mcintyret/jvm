package com.mcintyret.jvm.core.opcode.condition;

import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public abstract class UnaryCondition extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        if (conditionMet(ctx.getStack().pop())) {
            ctx.getByteIterator().seek(ctx.getByteIterator().nextShort());
        }
    }

    protected abstract boolean conditionMet(int pop);

}
