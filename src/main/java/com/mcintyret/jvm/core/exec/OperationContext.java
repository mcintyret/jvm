package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.util.ByteIterator;

public interface OperationContext {

    ByteIterator getByteIterator();

    Variables getLocalVariables();

    ConstantPool getConstantPool();

    VariableStack getStack();

    ExecutionStack getExecutionStack();

    Thread getThread();

    Method getMethod();

}
