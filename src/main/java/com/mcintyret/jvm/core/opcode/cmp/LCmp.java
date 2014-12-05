package com.mcintyret.jvm.core.opcode.cmp;

import com.google.common.primitives.Longs;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.opcode.OpCode;

class LCmp extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        long b = stack.popLong();
        long a = stack.popLong();
        stack.pushInt(Longs.compare(a, b));
    }

    @Override
    public byte getByte() {
        return (byte) 0x94;
    }
}
