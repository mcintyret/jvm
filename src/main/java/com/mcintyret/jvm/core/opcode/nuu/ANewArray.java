package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

class ANewArray extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        AbstractClassObject clazz = ctx.getConstantPool().getClassObject(ctx.getByteIterator().nextShort());

        ctx.getStack().pushChecked(Heap.allocate(Utils.newArray(clazz.getType(), ctx.getStack().popInt())), SimpleType.REF);
    }

    @Override
    public byte getByte() {
        return (byte) 0xBD;
    }
}
