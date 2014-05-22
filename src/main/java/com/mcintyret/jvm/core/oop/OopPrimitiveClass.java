package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.domain.SimpleType;

public class OopPrimitiveClass extends OopClass {

    private final SimpleType type;

    public OopPrimitiveClass(ClassObject classObject, int[] fields, SimpleType type) {
        super(classObject, fields);
        this.type = type;
    }

    public SimpleType getType() {
        return type;
    }
}
