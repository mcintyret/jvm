package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Oop;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class New extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ClassObject clazz = (ClassObject) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        Field[] fields = clazz.getInstanceFields();
        Field lastField = fields[fields.length - 1];
        int size = lastField.getOffset() + lastField.getType().getWidth();

        Oop newOop = new Oop(clazz, null, new int[size]);
        ctx.getStack().push(Heap.allocate(newOop));
    }

    @Override
    public byte getByte() {
        return (byte) 0xBB;
    }
}
