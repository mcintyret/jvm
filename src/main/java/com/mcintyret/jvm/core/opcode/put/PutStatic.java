package com.mcintyret.jvm.core.opcode.put;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class PutStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();

        Field field = ctx.getConstantPool().getField(ctx.getByteIterator().nextShort());

        int[] fields = field.getClassObject().getStaticFieldValues();

        int offset = field.getOffset();
        if (field.getType().getSimpleType().isDoubleWidth()) {
            fields[offset + 1] = stack.pop();
        }
        fields[offset] = stack.pop();
    }


    @Override
    public byte getByte() {
        return (byte) 0xB3;
    }
}
