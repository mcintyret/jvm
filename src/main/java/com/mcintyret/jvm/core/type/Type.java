package com.mcintyret.jvm.core.type;

import com.mcintyret.jvm.core.oop.OopClassClass;

public interface Type {

    boolean isPrimitive();

    OopClassClass getOopClassClass();

    boolean isArray();

    boolean isInterface();

    boolean isDoubleWidth();

    default int getWidth() {
        return isDoubleWidth() ? 2 : 1;
    }

}
