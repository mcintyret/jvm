package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.type.SimpleType;

public interface FloatTyped extends Typed {

    @Override
    default SimpleType getType() {
        return SimpleType.FLOAT;
    }

}
