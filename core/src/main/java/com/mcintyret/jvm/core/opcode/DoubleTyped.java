package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.type.SimpleType;

public interface DoubleTyped extends Typed {

    @Override
    default SimpleType getType() {
        return SimpleType.DOUBLE;
    }
}
