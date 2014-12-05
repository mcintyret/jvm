package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class FieldOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        Field field = ctx.getConstantPool().getField(ctx.getByteIterator().nextShortUnsigned());

        doExecute(field, ctx);
    }

    protected abstract void doExecute(Field field, OperationContext ctx);
}
