package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;

abstract class SingleWidthAStore extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int val = stack.popChecked(getType());
        int index = stack.popInt();
        OopArray array = stack.popOop();

        array.getFields()[index] = val;
    }

}
