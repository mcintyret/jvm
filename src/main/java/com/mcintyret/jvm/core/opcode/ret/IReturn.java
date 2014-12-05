package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.type.SimpleType;

class IReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xAC;
    }

    @Override
    protected SimpleType getType() {
        return SimpleType.INT;
    }
}
