package com.mcintyret.jvm.core.constantpool;

public class ConstantPool {

    private final Object[] constantPool;

    public ConstantPool(Object[] constantPool) {
        this.constantPool = constantPool;
    }

    public Object get(int i) {
        return constantPool[i];
    }
}
