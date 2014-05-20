package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Method;
import java.util.HashMap;
import java.util.Map;

public class InterfaceMethodReference extends MethodReference {

    private final Map<String, Method> methodMap = new HashMap<>();

    public InterfaceMethodReference(ClassObject classObject, int methodIndex) {
        super(classObject, methodIndex, false);
    }

    public void registerMethodForImplementation(String className, Method method) {
        if (methodMap.put(className, method) != null) {
            throw new IllegalStateException("Method already registered for class " + className);
        }
    }

    public Method getMethodForImplementation(String className) {
        return methodMap.get(className);
    }
}
