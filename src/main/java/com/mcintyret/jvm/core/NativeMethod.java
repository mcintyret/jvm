package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class NativeMethod extends Method {

    private final java.lang.reflect.Method method;

    public NativeMethod(MethodSignature methodSignature, Set<Modifier> modifiers, java.lang.reflect.Method method) {
        super(null, methodSignature, modifiers, -1);
        this.method = method;
    }

    public java.lang.reflect.Method getMethod() {
        return method;
    }
}
