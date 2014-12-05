package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;

public interface OperationContext {

    ByteIterator getByteIterator();

    Variable[] getLocalVars();

    ConstantPool getConstantPool();

    VariableStack getStack();

    ExecutionStack getExecutionStack();

    Thread getThread();

    Method getMethod();

}
