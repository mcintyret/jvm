package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class NativeMethod extends Method {

    private final NativeImplementation nativeImplementation;

    public NativeMethod(MethodSignature methodSignature, Set<Modifier> modifiers, NativeImplementation nativeImplementation) {
        super(null, methodSignature, modifiers, 0);
        this.nativeImplementation = nativeImplementation;
    }

    public NativeImplementation getNativeImplementation() {
        return nativeImplementation;
    }
}
