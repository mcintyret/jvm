package com.mcintyret.jvm.core.domain;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.oop.OopClass;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

public interface Type {

    static final ClassObject CLASS_CLASS = getDefaultClassLoader().getClassObject("java/lang/Class");

    SimpleType getSimpleType();

    boolean isPrimitive();

    OopClass getClassOop();

}
