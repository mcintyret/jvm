package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.oop.Oop;

class GetStatic extends Get {

    @Override
    public byte getByte() {
        return (byte) 0xB2;
    }

    @Override
    protected Oop getOop(OperationContext ctx) {
        return null;
    }
}
