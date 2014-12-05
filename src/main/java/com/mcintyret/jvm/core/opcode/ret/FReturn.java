package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.type.SimpleType;

class FReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAE;
    }

    @Override
    protected SimpleType getType() {
        return SimpleType.FLOAT;
    }
}
