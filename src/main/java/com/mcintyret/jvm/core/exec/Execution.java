package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.util.ByteBufferIterator;
import com.mcintyret.jvm.core.util.ByteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Execution implements OperationContext {

    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);

    public final static AtomicInteger TOTAL_OPCODES_EXECUTED = new AtomicInteger();

    public static Execution current;

    private final Method method;

    private final ByteIterator byteIterator;

    private final Variables localVariables;

    private final ConstantPool constantPool;

    private final Thread thread;

    private final VariableStack stack = new VariableStackImpl();

    public Execution(Method method, Variables localVariables, ConstantPool constantPool, Thread thread) {
        this.method = method;
        this.byteIterator = method.getCode() == null ? null : new ByteBufferIterator(method.getCode().getCode());
        this.localVariables = localVariables;
        this.constantPool = constantPool;
        this.thread = thread;
    }

    public void executeNextInstruction() {
        current = this;
        int pos = byteIterator.getPos();
        OpCode opCode = OpCodes.getOpcode(byteIterator.nextByte());
        LOG.debug(String.format("%4d: %s", pos, opCode));

        if (opCode.isSafePoint()) {
            Heap.atSafePoint();
        }

        opCode.execute(this);
        TOTAL_OPCODES_EXECUTED.incrementAndGet();
    }

    @Override
    public ByteIterator getByteIterator() {
        return byteIterator;
    }

    @Override
    public Variables getLocalVariables() {
        return localVariables;
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
        return thread.getCurrentStack();
    }

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
