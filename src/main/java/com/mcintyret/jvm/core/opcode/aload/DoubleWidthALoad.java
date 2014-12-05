package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class DoubleWidthALoad extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        int index = stack.popInt() * 2;
        OopArray array = stack.popOop();

        SimpleType type = getType();
        stack.pushSingleWidth(array.getFields()[index], type);
        stack.pushSingleWidth(array.getFields()[index + 1], type);
    }

}
