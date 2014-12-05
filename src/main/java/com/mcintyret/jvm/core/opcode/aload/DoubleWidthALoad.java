package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class DoubleWidthALoad extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int index = stack.popInt() * 2;
        OopArray array = stack.popOop();

        stack.push(array.getFields()[index]);
        stack.push(array.getFields()[index + 1]);
    }

}
