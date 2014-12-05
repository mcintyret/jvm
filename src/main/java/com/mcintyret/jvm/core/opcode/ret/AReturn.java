package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.type.SimpleType;

class AReturn extends SingleWidthReturn {

    @Override
    public byte getByte() {
        return (byte) 0xB0;
    }

    @Override
    protected SimpleType getType() {
        return SimpleType.REF;
    }
}
