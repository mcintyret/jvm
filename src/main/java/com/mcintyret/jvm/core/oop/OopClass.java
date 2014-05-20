package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.MarkRef;
import com.mcintyret.jvm.core.clazz.ClassObject;

public class OopClass extends Oop {

    private final ClassObject classObject;

    public OopClass(ClassObject classObject, MarkRef markRef, int[] fields) {
        super(markRef, fields);
        this.classObject = classObject;
    }

    @Override
    public ClassObject getClassObject() {
        return classObject;
    }
}
