package com.mcintyret.jvm.core;

import java.util.concurrent.atomic.AtomicInteger;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class ExecutionStackElement implements OperationContext {

    public final static AtomicInteger TOTAL_OPCODES_EXECUTED = new AtomicInteger();

    public static ExecutionStackElement current;

    private final Method method;

    private final ByteIterator byteIterator;

    private final int[] localVars;

    private final ConstantPool constantPool;

    private final ExecutionStack executionStack;

    private final WordStack stack = new WordStack();

    public ExecutionStackElement(Method method, int[] localVars, ConstantPool constantPool, ExecutionStack executionStack) {
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
//        if (method.getSignature().getName().equals("filterFields")) {
            if (true) {
            System.out.println(String.format("%4d: %s", pos, opCode));
        }
        opCode.execute(this);
        TOTAL_OPCODES_EXECUTED.incrementAndGet();
    }

    @Override
    public ByteIterator getByteIterator() {
        return byteIterator;
    }

    @Override
    public int[] getLocalVars() {
        return localVars;
    }

    @Override
    public ConstantPool getConstantPool() {
        return constantPool;
    }

    @Override
    public WordStack getStack() {
        return stack;
    }

    @Override
    public ExecutionStack getExecutionStack() {
        return executionStack;
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
