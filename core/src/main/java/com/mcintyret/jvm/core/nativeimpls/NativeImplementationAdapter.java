package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.type.MethodSignature;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public abstract class NativeImplementationAdapter implements NativeImplementation {

    private final String name;

    private final MethodSignature methodSignature;

    public NativeImplementationAdapter(String name, MethodSignature methodSignature) {
        this.name = name;
        this.methodSignature = methodSignature;
    }

    @Override
    public final String getClassName() {
        return name;
    }

    @Override
    public final MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
