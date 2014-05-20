package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class Method {

    private final ByteCode byteCode;

    private final MethodSignature methodSignature;

    private final Set<Modifier> modifiers;

    private final int maxLocalVariables;

    private MethodReference methodReference;

    public Method(ByteCode byteCode, MethodSignature methodSignature, Set<Modifier> modifiers, int maxLocalVariables) {
        this.byteCode = byteCode;
        this.methodSignature = methodSignature;
        this.modifiers = modifiers;
        this.maxLocalVariables = maxLocalVariables;
    }

    public ByteCode getByteCode() {
        return byteCode;
    }

    public MethodSignature getSignature() {
        return methodSignature;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public int getMaxLocalVariables() {
        return maxLocalVariables;
    }

    public MethodReference getMethodReference() {
        return methodReference;
    }

    public void setMethodReference(MethodReference methodReference) {
        this.methodReference = methodReference;
    }
}
