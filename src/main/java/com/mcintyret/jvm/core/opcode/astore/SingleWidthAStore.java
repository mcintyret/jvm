package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;

abstract class SingleWidthAStore extends OpCode {
    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int val = stack.popInt(); // Does this need more type safety?
        int index = stack.popInt();
        OopArray array = stack.popOop();

        array.getFields()[index] = val;
    }

}
