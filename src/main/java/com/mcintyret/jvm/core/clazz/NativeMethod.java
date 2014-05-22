package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.Set;

public class NativeMethod extends Method {

    private final NativeImplementation nativeImplementation;

    public NativeMethod(Set<Modifier> modifiers, Attributes attributes, MethodSignature signature, NativeImplementation nativeImplementation) {
        super(modifiers, attributes, signature);
        this.nativeImplementation = nativeImplementation;
    }

    public NativeImplementation getNativeImplementation() {
        return nativeImplementation;
    }
}
