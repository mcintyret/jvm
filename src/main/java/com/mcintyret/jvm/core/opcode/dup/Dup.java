package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class Dup extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        stack.push(stack.peek());
    }

    @Override
    public byte getByte() {
        return 0x59;
    }
}
