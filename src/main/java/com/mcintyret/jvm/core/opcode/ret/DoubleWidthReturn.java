package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class DoubleWidthReturn extends BaseValueReturn {

    @Override
    protected final NativeReturn finalReturn(VariableStack stack) {
        return NativeReturn.forLong(stack.popDoubleWidth(getType()));
    }

    @Override
    protected final void pushReturnVal(VariableStack lower, VariableStack upper) {
        SimpleType type = getType();
        lower.pushDoubleWidth(upper.popDoubleWidth(type), type);
    }
}
