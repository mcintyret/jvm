package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.type.SimpleType;

public interface ShortTyped extends Typed {

    @Override
    default SimpleType getType() {
        return SimpleType.SHORT;
    }

}
