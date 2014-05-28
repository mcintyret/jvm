package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;

abstract class SingleWidthReturn extends BaseReturn {

    @Override
    protected final NativeReturn finalReturn(WordStack stack) {
        return NativeReturn.forInt(stack.pop());
    }

    @Override
    protected final void pushReturnVal(WordStack lower, WordStack upper) {
        lower.push(upper.pop());
    }
}