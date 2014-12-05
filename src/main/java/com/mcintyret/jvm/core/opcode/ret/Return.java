package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;

class Return extends BaseReturn {

    @Override
    public byte getByte() {
        return (byte) 0xB1;
    }

    @Override
    protected void returnValue(OperationContext ctx) {
        if (ctx.getExecutionStack().peek() == null) {
            ctx.getExecutionStack().setFinalReturn(NativeReturn.forVoid());
        }
    }
}
