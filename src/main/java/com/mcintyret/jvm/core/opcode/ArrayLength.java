package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.OopArray;
import com.mcintyret.jvm.core.WordStack;

class ArrayLength extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push(((OopArray) Heap.getOop(stack.pop())).getLength());
    }

    @Override
    public byte getByte() {
        return (byte) 0xBE;
    }
}
