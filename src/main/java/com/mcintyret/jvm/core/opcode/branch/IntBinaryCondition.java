package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.type.SimpleType;

abstract class IntBinaryCondition extends BinaryCondition {

    @Override
    protected SimpleType getType() {
        return SimpleType.INT;
    }
}
