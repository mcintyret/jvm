package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.OperationContext;

class Return extends BaseReturn {


    @Override
    public byte getByte() {
        return (byte) 0xB1;
    }

    @Override
    protected void returnValue(OperationContext ctx) {
        // Do nothing
    }
}
