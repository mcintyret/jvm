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

        int val2 = stack.pop();
        int val1 = stack.pop();

        int index = stack.pop() * 2;

        OopArray array = (OopArray) Heap.getOop(stack.pop());

        array.getFields()[index] = val1;
        array.getFields()[index] = val2;
    }

}
