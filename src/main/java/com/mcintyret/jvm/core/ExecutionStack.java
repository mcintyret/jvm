package com.mcintyret.jvm.core;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class ExecutionStack {

    private final Deque<ExecutionStackElement> stack = new ArrayDeque<>();

    public void execute() {
        ExecutionStackElement current = null;
        ExecutionStackElement prev;
        while(true) {
            prev = current;
            current = stack.peek();
            if (current == null) {
                System.out.println("Last variables: " + Arrays.toString(prev.getVariables()));
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
}
