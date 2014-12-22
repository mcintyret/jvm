package com.mcintyret.jvm.core.type;

import com.mcintyret.jvm.core.oop.OopClassClass;

public interface Type {

    boolean isPrimitive();

    OopClassClass getOopClassClass(boolean gc);

    default OopClassClass getOopClassClass() {
        return getOopClassClass(false);
    }

    boolean isArray();

    boolean isInterface();

    boolean isDoubleWidth();

    SimpleType asSimpleType();

    default int getWidth() {
        return isDoubleWidth() ? 2 : 1;
    }

}
