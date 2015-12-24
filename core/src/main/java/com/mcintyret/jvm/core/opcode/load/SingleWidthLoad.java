package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.ByteIterator;

abstract class SingleWidthLoad extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        SimpleType type = getType();

        ctx.getStack().pushSingleWidth(ctx.getLocalVariables().getCheckedValue(index, type), type);
    }

    protected abstract int getIndex(ByteIterator bytes);
}
