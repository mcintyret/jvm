package com.mcintyret.jvm.core.opcode.dup;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class Dup_X2 extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        int one = stack.pop();
        int two = stack.pop();
        int three = stack.pop();
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
