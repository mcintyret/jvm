package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class New extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ClassObject clazz = (ClassObject) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        ctx.getStack().push(Heap.allocate(clazz.newObject()));
    }

    @Override
    public byte getByte() {
        return (byte) 0xBB;
    }
}
