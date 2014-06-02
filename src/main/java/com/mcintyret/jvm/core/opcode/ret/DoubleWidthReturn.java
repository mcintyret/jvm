package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;

abstract class DoubleWidthReturn extends BaseValueReturn {

    @Override
    protected final NativeReturn finalReturn(WordStack stack) {
        return NativeReturn.forLong(stack.popLong());
    }

    @Override
    protected final void pushReturnVal(WordStack lower, WordStack upper) {
        lower.push(upper.popLong());
    }
}
