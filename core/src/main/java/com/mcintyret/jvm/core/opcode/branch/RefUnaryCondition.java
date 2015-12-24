package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.type.SimpleType;

abstract class RefUnaryCondition extends UnaryCondition {

    @Override
    public SimpleType getType() {
        return SimpleType.REF;
    }

}
