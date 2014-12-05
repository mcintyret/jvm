package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

class NewArray extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        SimpleType type = SimpleType.forByte(ctx.getByteIterator().nextByte());

        ctx.getStack().pushSingleWidth(Heap.allocate(Utils.newArray(type, ctx.getStack().popInt())), SimpleType.REF);
    }

    @Override
    public byte getByte() {
        return (byte) 0xBC;
    }
}
