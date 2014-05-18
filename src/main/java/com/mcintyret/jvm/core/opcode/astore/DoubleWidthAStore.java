package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.OopArray;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class DoubleWidthAStore extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        OopArray array = (OopArray) Heap.getOop(stack.pop());

        int index = stack.pop() * 2;

        int val2 = stack.pop();
        int val1 = stack.pop();

        array.getFields()[index] = val1;
        array.getFields()[index] = val2;
    }

}
