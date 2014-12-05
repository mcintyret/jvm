package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.type.SimpleType;

public interface IntTyped extends Typed {

    @Override
    default SimpleType getType() {
        return SimpleType.INT;
    }

}
