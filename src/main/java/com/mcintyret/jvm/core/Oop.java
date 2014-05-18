package com.mcintyret.jvm.core;

public class Oop {

    private final ClassObject classObject;

    private final MarkRef markRef;

    private final int[] fields;

    public Oop(ClassObject classObject, MarkRef markRef, int[] fields) {
        this.classObject = classObject;
        this.markRef = markRef;
        this.fields = fields;
    }

    public ClassObject getClassObject() {
        return classObject;
    }

    public MarkRef getMarkRef() {
        return markRef;
    }

    public int[] getFields() {
        return fields;
    }
}
