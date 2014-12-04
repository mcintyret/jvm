package com.mcintyret.jvm.core.domain;

import com.mcintyret.jvm.core.oop.OopClassClass;

public interface Type {

    static final String CLASS_CLASS = "java/lang/Class";

    boolean isPrimitive();

    OopClassClass getOopClassClass();

    boolean isArray();

    boolean isInterface();

    boolean isDoubleWidth();

    default int getWidth() {
        return isDoubleWidth() ? 2 : 1;
    }

}
