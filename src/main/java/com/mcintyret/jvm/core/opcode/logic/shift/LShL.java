package com.mcintyret.jvm.core.opcode.logic.shift;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class LShL extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        int shift = stack.pop();
        long l = stack.popLong();
        stack.push(l << shift);
    }

    @Override
    public byte getByte() {
        return 0x79;
    }
}
