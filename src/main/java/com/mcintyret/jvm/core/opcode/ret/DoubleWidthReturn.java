package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class DoubleWidthReturn extends BaseReturn {

    @Override
    protected final NativeReturn finalReturn(WordStack stack) {
        return NativeReturn.forLong(stack.popLong());
    }

    @Override
    protected final void pushReturnVal(WordStack lower, WordStack upper) {
        lower.push(upper.popLong());
    }
}
