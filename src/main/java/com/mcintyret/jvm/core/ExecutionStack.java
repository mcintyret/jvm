package com.mcintyret.jvm.core;

import java.util.Stack;

public class ExecutionStack {

    private final Stack<ExecutionStackElement> stack;

    public ExecutionStack(Stack<ExecutionStackElement> stack) {
        this.stack = stack;
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
                if (current.isComplete()) {
                    stack.pop();
                }
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
