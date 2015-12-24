package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.oop.Oop;

class GetField extends Get {

    @Override
    public byte getByte() {
        return (byte) 0xB4;
    }

    @Override
    protected Oop getOop(OperationContext ctx) {
        return ctx.getStack().popOop();
    }
}
