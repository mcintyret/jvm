package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OperationContext;

class GetField extends Get {

    @Override
    public byte getByte() {
        return (byte) 0xB4;
    }

    @Override
    protected Oop getOop(OperationContext ctx) {
        return Heap.getOop(ctx.getStack().pop());
    }
}
