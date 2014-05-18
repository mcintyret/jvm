package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class Method {

    private final ByteCode byteCode;

    private final MethodSignature methodSignature;

    private final Set<Modifier> modifiers;

    public Method(ByteCode byteCode, MethodSignature methodSignature, Set<Modifier> modifiers) {
        this.byteCode = byteCode;
        this.methodSignature = methodSignature;
        this.modifiers = modifiers;
    }

    public ByteCode getByteCode() {
        return byteCode;
    }

    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }
}
