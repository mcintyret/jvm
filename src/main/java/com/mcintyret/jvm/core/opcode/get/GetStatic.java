package com.mcintyret.jvm.core.opcode.get;

import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class GetStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        FieldReference ref = ctx.getConstantPool().getFieldReference(ctx.getByteIterator().nextShort());
        Field field = ref.getStaticField();

        Utils.getField(ctx.getStack(), ref.getClassObject().getStaticFieldValues(), field);
    }

    @Override
    public byte getByte() {
        return (byte) 0xB2;
    }

}
