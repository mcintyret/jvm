package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.MarkRef;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;

public abstract class Oop {

    private final MarkRef markRef = new MarkRef();

    private final int[] fields;

    private int address;

    public Oop(int[] fields) {
        this.fields = fields;
    }

    public abstract AbstractClassObject getClassObject();

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