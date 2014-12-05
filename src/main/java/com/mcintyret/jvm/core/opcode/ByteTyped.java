package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.type.SimpleType;

public interface ByteTyped extends Typed {

    @Override
    default SimpleType getType() {
        return SimpleType.BYTE;
    }

}
