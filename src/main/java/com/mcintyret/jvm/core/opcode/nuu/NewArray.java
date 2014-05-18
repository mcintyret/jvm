package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class NewArray extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        SimpleType type = SimpleType.forByte(ctx.getByteIterator().next());

        ctx.getStack().push(Heap.allocate(Utils.newArray(type, ctx.getStack().pop())));
    }

    @Override
    public byte getByte() {
        return (byte) 0xBC;
    }
}
