package com.mcintyret.jvm.core.opcode.put;

import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

import static com.mcintyret.jvm.core.Utils.putField;

public class PutStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();

        FieldReference fieldRef = (FieldReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());
        Field field = fieldRef.getStaticField();

        int[] fields = fieldRef.getClassObject().getStaticFieldValues();

        putField(stack, fields, field);
    }


    @Override
    public byte getByte() {
        return (byte) 0xB3;
    }
}
