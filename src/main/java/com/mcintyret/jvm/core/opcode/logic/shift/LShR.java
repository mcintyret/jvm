package com.mcintyret.jvm.core.opcode.logic.shift;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class LShR extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push(stack.popLong() >> stack.pop());
    }

    @Override
    public byte getByte() {
        return 0x7B;
    }
}
