package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;

abstract class SingleWidthALoad extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int index = stack.popInt();

        OopArray array = stack.popOop();

        stack.pushChecked(array.getFields()[index], getType());
    }

}
