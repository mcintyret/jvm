package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.opcode.OperationContext;

import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionStackElement implements OperationContext {

    public final static AtomicInteger TOTAL_OPCODES_EXECUTED = new AtomicInteger();

    private final ByteIterator byteIterator;

    private final int[] variables;

    private final ConstantPool constantPool;

    private final ExecutionStack executionStack;

    private final WordStack stack = new WordStack();

    public ExecutionStackElement(ByteCode byteCode, int[] variables, ConstantPool constantPool, ExecutionStack executionStack) {
        this.byteIterator = byteCode.byteIterator();
        this.variables = variables;
        this.constantPool = constantPool;
        this.executionStack = executionStack;
    }

    public void executeNextInstruction() {
        int pos = byteIterator.getPos();
        OpCode opCode = OpCodes.getOpcode(byteIterator.nextByte());
        System.out.println(String.format("%4d: %s", pos, opCode));
        opCode.execute(this);
        TOTAL_OPCODES_EXECUTED.incrementAndGet();
    }

    @Override
    public ByteIterator getByteIterator() {
        return byteIterator;
    }

    @Override
    public int[] getVariables() {
        return variables;
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
}
