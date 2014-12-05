package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;

class New extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ClassObject clazz = (ClassObject) ctx.getConstantPool().getClassObject(ctx.getByteIterator().nextShort());

        ctx.getStack().pushChecked(Heap.allocate(clazz.newObject()), SimpleType.REF);
    }

    @Override
    public byte getByte() {
        return (byte) 0xBB;
    }
}
