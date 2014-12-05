package com.mcintyret.jvm.core.opcode.logic.shift;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

public class LShR extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        int shift = stack.pop();
        long l = stack.popLong();
        stack.push(l >> shift);
    }

    @Override
    public byte getByte() {
        return 0x7B;
    }
}
