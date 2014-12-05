package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup_X2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        Variable one = stack.pop();
        Variable two = stack.pop();
        Variable three = stack.pop();

        stack.push(one);
        stack.push(three);
        stack.push(two);
        stack.push(one);
    }

    @Override
    public byte getByte() {
        return 0x5B;
    }

}
