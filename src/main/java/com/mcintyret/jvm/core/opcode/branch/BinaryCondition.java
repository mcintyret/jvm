package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class BinaryCondition extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        int jump = ctx.getByteIterator().nextShort();
        if (conditionMet(ctx.getStack().popChecked(getType()), ctx.getStack().popChecked(getType()))) {
            ctx.getByteIterator().seek(jump - 3);
        }
    }

    protected abstract boolean conditionMet(int a, int b);

    protected abstract SimpleType getType();
}
