package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class SingleWidthAStore extends OpCode {
    @Override
    public final void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();

        int val = stack.pop();
        int index = stack.pop();
        OopArray array = (OopArray) Heap.getOop(stack.pop());


        array.getFields()[index] = val;
    }

}
