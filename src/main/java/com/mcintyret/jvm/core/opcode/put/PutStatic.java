package com.mcintyret.jvm.core.opcode.put;

import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class PutStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();

        FieldReference fieldRef = ctx.getConstantPool().getFieldReference(ctx.getByteIterator().nextShort());
        Field field = fieldRef.getStaticField();

        int[] fields = fieldRef.getClassObject().getStaticFieldValues();

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
