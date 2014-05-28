package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InterfaceMethod extends Method {

    private final Map<String, Method> methodMap = new HashMap<>();

    public InterfaceMethod(Set<Modifier> modifiers, Attributes attributes, MethodSignature signature) {
        super(modifiers, attributes, signature);
    }

    public void registerMethodForImplementation(String className, Method method) {
        if (methodMap.put(className, method) != null) {
            throw new IllegalStateException("Method already registered for class " + className);
        }
    }

    public Method getMethodForImplementation(ClassObject classObject) {
        ClassObject obj = classObject;
        Method impl;
        do {
            impl = methodMap.get(obj.getClassName());
            obj = obj.getSuperClass();
        } while (impl == null);

        return impl;
    }

}
