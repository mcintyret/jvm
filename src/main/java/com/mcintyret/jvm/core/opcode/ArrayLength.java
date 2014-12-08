package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.OopArray;

class ArrayLength extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        stack.pushInt(stack.<OopArray>popOop().getLength());
    }

    @Override
    public byte getByte() {
        return (byte) 0xBE;
    }
}
