package com.mcintyret.jvm.core.opcode.get;

import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class GetStatic extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        Field field = ctx.getConstantPool().getField(ctx.getByteIterator().nextShortUnsigned());

        Utils.getField(ctx.getStack(), field.getClassObject().getStaticFieldValues(), field);
    }

    @Override
    public byte getByte() {
        return (byte) 0xB2;
    }

}
