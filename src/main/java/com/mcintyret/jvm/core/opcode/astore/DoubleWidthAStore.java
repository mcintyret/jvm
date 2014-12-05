package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStackImpl;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class DoubleWidthAStore extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStackImpl stack = ctx.getStack();

        int val2 = stack.pop();
        int val1 = stack.pop();

        int index = stack.pop() * 2;

        OopArray array = stack.pop().getOop();

        array.getFields()[index] = val1;
        array.getFields()[index] = val2;
    }

}
