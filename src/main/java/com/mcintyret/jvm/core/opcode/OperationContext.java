package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.constantpool.ConstantPool;

public interface OperationContext {

    ByteIterator getByteIterator();

    int[] getLocalVars();

    ConstantPool getConstantPool();

    WordStack getStack();

    ExecutionStack getExecutionStack();
}
