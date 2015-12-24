package com.mcintyret.jvm.core.opcode.constant;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

class AConst_Null extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ctx.getStack().push(Heap.NULL_POINTER);
    }

    @Override
    public byte getByte() {
        return 0x01;
    }
}
