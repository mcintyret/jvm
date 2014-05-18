package com.mcintyret.jvm.core;

public class Oop {

    private final ClassObject classObject;

    private final MarkRef markRef;

    private final int[] fields;

    private int address;

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

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
