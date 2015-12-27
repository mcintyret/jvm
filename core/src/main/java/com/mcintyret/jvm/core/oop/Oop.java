package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.exec.Variables;

public abstract class Oop {

    public static final int UNALLOCATED_ADDRESS = -1;

    private final MarkRef markRef = new MarkRef();

    private final Variables fields;

    private int address = UNALLOCATED_ADDRESS;

    public Oop(Variables fields) {
        this.fields = fields;
    }

    public abstract AbstractClassObject getClassObject();

    public MarkRef getMarkRef() {
        return markRef;
    }

    public Variables getFields() {
        return fields;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getClassObject().getType() + "@" + getAddress();
    }
}
