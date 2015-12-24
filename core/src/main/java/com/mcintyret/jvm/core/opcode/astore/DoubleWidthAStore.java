package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.Typed;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class DoubleWidthAStore extends OpCode implements Typed {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        SimpleType type = getType();

        int val2 = stack.popSingleWidth(type);
        int val1 = stack.popSingleWidth(type);

        int index = stack.popInt() * 2;

        OopArray array = stack.popOop();

        array.getFields()[index] = val1;
        array.getFields()[index] = val2;
    }

}
