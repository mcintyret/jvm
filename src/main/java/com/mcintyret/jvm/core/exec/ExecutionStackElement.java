package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.util.ByteBufferIterator;
import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.thread.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionStackElement implements OperationContext {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionStackElement.class);

    public final static AtomicInteger TOTAL_OPCODES_EXECUTED = new AtomicInteger();

    public static ExecutionStackElement current;

    private final Method method;

    private final ByteIterator byteIterator;

    private final Variable[] localVars;

    private final ConstantPool constantPool;

    private final ExecutionStack executionStack;

    private final VariableStack stack = new VariableStack();

    public ExecutionStackElement(Method method, Variable[] localVars, ConstantPool constantPool, ExecutionStack executionStack) {
        this.method = method;
        this.byteIterator = method.getCode() == null ? null : new ByteBufferIterator(method.getCode().getCode());
        this.localVars = localVars;
        this.constantPool = constantPool;
        this.executionStack = executionStack;
    }

    public void executeNextInstruction() {
        current = this;
        int pos = byteIterator.getPos();
        OpCode opCode = OpCodes.getOpcode(byteIterator.nextByte());
        LOG.debug(String.format("%4d: %s", pos, opCode));
        opCode.execute(this);
        TOTAL_OPCODES_EXECUTED.incrementAndGet();
    }

    @Override
    public ByteIterator getByteIterator() {
        return byteIterator;
    }

    @Override
    public Variable[] getLocalVars() {
        return localVars;
    }

    @Override
    public ConstantPool getConstantPool() {
        return constantPool;
    }

    @Override
    public VariableStack getStack() {
        return stack;
    }

    @Override
    public ExecutionStack getExecutionStack() {
        return executionStack;
    }

    @Override
    public Thread getThread() {
        return executionStack.getThread();
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
