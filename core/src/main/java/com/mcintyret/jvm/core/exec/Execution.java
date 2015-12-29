package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OpCodes;
import com.mcintyret.jvm.core.util.ByteBufferIterator;
import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.parse.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Execution implements OperationContext {

    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);

    public final static AtomicInteger TOTAL_OPCODES_EXECUTED = new AtomicInteger();

    private final Method method;

    private final ByteIterator byteIterator;

    private final Variables localVariables;

    private final ConstantPool constantPool;

    private final Thread thread;

    private final VariableStack stack = new VariableStackImpl();

    private final Lock synchronizedMethodLock;

    public Execution(Method method, Variables localVariables, Thread thread) {
        this.method = method;
        this.byteIterator = method.getCode() == null ? null : new ByteBufferIterator(method.getCode().getCode());
        this.localVariables = localVariables;
        this.constantPool = method.getClassObject().getConstantPool();
        this.thread = thread;

        // Apparently synchronized methods don't create monitorenter/exit bytecodes, so we have to do it manually
        // (synchronized blocks do, however)
        if (method.hasModifier(Modifier.SYNCHRONIZED)) {
            Oop target = method.isStatic() ?
                method.getClassObject().getOop() :
                localVariables.getOop(0);

            synchronizedMethodLock = target.getMarkRef().getMonitor();
        } else {
            synchronizedMethodLock = null;
        }
    }

    void prepare() {
        if (synchronizedMethodLock != null) {
            if (!synchronizedMethodLock.tryLock()) {
                Heap.threadSleeping();
                synchronizedMethodLock.lock();
                Heap.threadWaking();
            }
        }
    }

    public void executeNextInstruction() {
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

    @Override
    public void onComplete() {
        if (synchronizedMethodLock != null) {
            synchronizedMethodLock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Execution[" + method + "]";
    }
}
