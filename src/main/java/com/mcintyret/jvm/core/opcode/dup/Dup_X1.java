package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class Dup_X1 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        int top = stack.pop();
        int next = stack.pop();
        stack.push(top);
        stack.push(next);
        stack.push(top);
    }

    @Override
    public byte getByte() {
        return 0x5A;
    }
}
