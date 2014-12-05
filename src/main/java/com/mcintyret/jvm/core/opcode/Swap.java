package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
class Swap extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        int one = stack.pop();
        int two = stack.pop();

        stack.push(one);
        stack.push(two);
    }

    @Override
    public byte getByte() {
        return 0x5F;
    }
}
