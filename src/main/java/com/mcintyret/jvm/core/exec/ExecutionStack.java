package com.mcintyret.jvm.core.exec;

import java.util.ArrayDeque;
import java.util.Deque;

import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.thread.Thread;

public class ExecutionStack {

    private final Deque<ExecutionStackElement> stack = new ArrayDeque<>();

    private final Thread thread;

    private NativeReturn finalReturn;

    public ExecutionStack(Thread thread) {
        this.thread = thread;
    }

    public void execute() {
        ExecutionStackElement current;
        while(true) {
            current = stack.peek();
            if (current == null) {
                // Done!!
                break;
            } else {
                current.executeNextInstruction();
            }
        }
    }

    public void push(ExecutionStackElement element) {
        stack.push(element);
    }

    public void pop() {
        stack.pop();
    }

    public ExecutionStackElement peek() {
        return stack.peek();
    }

    public NativeReturn getFinalReturn() {
        return finalReturn;
    }

    public void setFinalReturn(NativeReturn finalReturn) {
        this.finalReturn = finalReturn;
    }

    // TODO: don't like having to expose this
    public Deque<ExecutionStackElement> getStack() {
        return stack;
    }

    public Thread getThread() {
        return thread;
    }
}

