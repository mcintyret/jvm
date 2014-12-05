package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

abstract class SingleWidthALoad extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int index = stack.pop();

        OopArray array = (OopArray) Heap.getOop(stack.pop());

        stack.push(array.getFields()[index]);
    }

}
