package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.Type;

public class OopArray extends Oop {

    private final Type type;

    public OopArray(ClassObject classObject, MarkRef markRef, int[] fields, Type type) {
        super(classObject, markRef, fields);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getLength() {
        return getFields().length / type.getSimpleType().getWidth();
    }
}
