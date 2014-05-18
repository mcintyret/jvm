package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Method;

public class MethodReference extends AbstractReference {

    public MethodReference(ClassObject classObject, int methodIndex) {
        super(classObject, methodIndex);
    }

    public Method getStaticMethod() {
        return getClassObject().getStaticMethod(getIndex());
    }

    public Method getInstanceMethod() {
        return getClassObject().getInstanceMethod(getIndex());
    }
}
