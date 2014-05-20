package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Method;

public class MethodReference extends AbstractReference {

    private final boolean isStatic;

    public MethodReference(ClassObject classObject, int methodIndex, boolean isStatic) {
        super(classObject, methodIndex);
        this.isStatic = isStatic;
    }

    public Method getMethod() {
        return isStatic ? getMethod(getIndex(), getClassObject().getStaticMethods()) :
            getMethod(getIndex(), getClassObject().getInstanceMethods());
    }

    private Method getMethod(int index, Method[] methods) {
        return methods[index];
    }
}
