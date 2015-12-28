package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup2_X1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        if (stack.peekType().isDoubleWidth()) {
            WideVariable top = stack.popWide();
            Variable next = popSingleWidth(stack);

            stack.pushWide(top);
            stack.push(next);
            stack.pushWide(top);
        } else {
            Variable one = popSingleWidth(stack);
            Variable two = popSingleWidth(stack);
            Variable three = popSingleWidth(stack);

            stack.push(two);
            stack.push(one);
            stack.push(three);
            stack.push(two);
            stack.push(one);
        }
    }

    @Override
    public byte getByte() {
        return 0x5D;
    }

    private Variable popSingleWidth(VariableStack stack) {
        Variable v = stack.pop();
        if (v.getType().isDoubleWidth()) {
            throw new AssertionError();
        }
        return v;
    }
}
