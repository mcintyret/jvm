package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.clazz.ClassObject;

abstract class AbstractReference {

    private final ClassObject classObject;

    private final int index;

    public AbstractReference(ClassObject classObject, int methodIndex) {
        this.classObject = classObject;
        this.index = methodIndex;
    }

    public ClassObject getClassObject() {
        return classObject;
    }

    public int getIndex() {
        return index;
    }
}
