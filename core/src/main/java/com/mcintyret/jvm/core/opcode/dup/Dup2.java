package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.opcode.OpCode;

class Dup2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();

        boolean wide = stack.peekType().isDoubleWidth();
        if (wide) {
            WideVariable v = stack.popWide();

            stack.pushWide(v);
            stack.pushWide(v);
        } else {
            Variable v1 = stack.pop();
            Variable v2 = stack.pop();

            stack.push(v2);
            stack.push(v1);
            stack.push(v2);
            stack.push(v1);
        }

    }

    @Override
    public byte getByte() {
        return 0x5C;
    }
}
