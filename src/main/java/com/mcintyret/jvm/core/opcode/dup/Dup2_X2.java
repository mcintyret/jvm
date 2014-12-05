package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup2_X2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        WideVariable one = stack.popWide();
        WideVariable two = stack.popWide();
        WideVariable three = stack.popWide();

        stack.pushWide(one);
        stack.pushWide(three);
        stack.pushWide(two);
        stack.pushWide(one);
    }

    @Override
    public byte getByte() {
        return 0x5E;
    }
}
