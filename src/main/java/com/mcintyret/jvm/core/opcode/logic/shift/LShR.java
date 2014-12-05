package com.mcintyret.jvm.core.opcode.logic.shift;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

public class LShR extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        int shift = stack.popInt();
        long l = stack.popLong();
        stack.pushLong(l >> shift);
    }

    @Override
    public byte getByte() {
        return 0x7B;
    }
}
