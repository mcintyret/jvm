package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.ByteIterator;

abstract class SingleWidthStore extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        int index = getIndex(ctx.getByteIterator());
        SimpleType type = getType();
        ctx.getLocalVariables().put(index, type, ctx.getStack().popChecked(type));
    }

    protected abstract int getIndex(ByteIterator bytes);
}
