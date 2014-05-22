package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class ExecutionStackElement implements OperationContext {

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
        OpCode opCode = OpCodes.getOpcode(byteIterator.nextByte());
        System.out.println(opCode);
        opCode.execute(this);
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
