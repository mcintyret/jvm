package com.mcintyret.jvm.core.opcode.get;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class GetField extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        int oopAddress = ctx.getStack().pop();

        Field field = ctx.getConstantPool().getField(ctx.getByteIterator().nextShort());

        Utils.getField(ctx.getStack(), Heap.getOop(oopAddress).getFields(), field);
    }

    @Override
    public byte getByte() {
        return (byte) 0xB4;
    }

}
