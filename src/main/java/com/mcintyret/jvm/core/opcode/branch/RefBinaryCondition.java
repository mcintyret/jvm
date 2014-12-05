package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.type.SimpleType;

abstract class RefBinaryCondition extends BinaryCondition {

    @Override
    protected SimpleType getType() {
        return SimpleType.REF;
    }
}
