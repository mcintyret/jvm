package com.mcintyret.jvm.core.thread;

import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopClass;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Thread {

    private final OopClass thisThread;

    private final String name;

    private final ExecutionStack executionStack;

    public Thread(OopClass thisThread, String name, Method entryPoint) {
        this(thisThread, name, entryPoint, entryPoint.newArgArray());
    }

    public Thread(OopClass thisThread, String name, Method entryPoint, int[] args) {
        this.thisThread = thisThread;
        this.name = name;
        this.executionStack = new ExecutionStack(this);
        executionStack.push(new ExecutionStackElement(entryPoint, args,
                entryPoint.getClassObject().getConstantPool(), executionStack));
    }

    public NativeReturn run() {
        executionStack.execute();
        return executionStack.getFinalReturn();
    }

    public OopClass getThisThread() {
        return thisThread;
    }

    public String getName() {
        return name;
    }
}
