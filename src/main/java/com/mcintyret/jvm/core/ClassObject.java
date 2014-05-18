package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.constantpool.ConstantPool;

public class ClassObject {

    private final ClassObject parent;

    private final ConstantPool constantPool;

    private final Method[] instanceMethods;

    private final Method[] staticMethods;

    private final Field[] instanceFields;

    private final Field[] staticFields;

    private final int[] staticFieldValues;

    public ClassObject(ClassObject parent, ConstantPool constantPool, Method[] instanceMethods, Method[] staticMethods,
                       Field[] instanceFields, Field[] staticFields, int[] staticFieldValues) {
        this.parent = parent;
        this.constantPool = constantPool;
        this.instanceMethods = instanceMethods;
        this.staticMethods = staticMethods;
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.staticFieldValues = staticFieldValues;
    }

    // For invokevirtual and invokespecial
    public Method getInstanceMethod(int i) {
        ClassObject co = this;
        while (true) {
            Method method = co.instanceMethods[i];
            if (method != null) {
                return method;
            }
            co = co.parent;
        }
    }

    // For invokestatic
    public Method getStaticMethod(int i) {
        return staticMethods[i];
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public Field[] getInstanceFields() {
        return instanceFields;
    }

    public int[] getStaticFieldValues() {
        return staticFieldValues;
    }

    public Field[] getStaticFields() {
        return staticFields;
    }
}
