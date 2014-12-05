package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.type.SimpleType;

abstract class IntUnaryCondition extends UnaryCondition {

    @Override
    public SimpleType getType() {
        return SimpleType.INT;
    }

}
