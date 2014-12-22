package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.nativeimpls.NativeReturn;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExecutionStack implements Iterable<Execution> {

    private final Deque<Execution> stack = new ArrayDeque<>();

    private NativeReturn finalReturn;

    public NativeReturn execute() {
        Execution current;
        while(true) {
            current = stack.peek();
            if (current == null) {
                // Done!!
                break;
            } else {
                current.executeNextInstruction();
            }
        }
        return checkNotNull(finalReturn);
    }

    public void push(Execution element) {
        stack.push(element);
    }

    public void pop() {
        stack.pop();
    }

    public Execution peek() {
        return stack.peek();
    }

    public void setFinalReturn(NativeReturn finalReturn) {
        this.finalReturn = finalReturn;
    }

    // TODO: don't like having to expose this
    public Deque<Execution> getStack() {
        return stack;
    }

    @Override
    public Iterator<Execution> iterator() {
        return stack.iterator();
    }
}

