package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;

public class OopClass extends Oop {

    private final ClassObject classObject;

    public OopClass(ClassObject classObject, int[] fields) {
        super(fields);
        this.classObject = classObject;
    }

    @Override
    public ClassObject getClassObject() {
        return classObject;
    }
}
