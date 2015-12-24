package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;

public class NativeMethod extends Method {

    private final NativeImplementation nativeImplementation;

    public NativeMethod(Set<Modifier> modifiers, Attributes attributes, MethodSignature signature, int offset, NativeImplementation nativeImplementation) {
        super(modifiers, attributes, signature, offset);
        this.nativeImplementation = nativeImplementation;
    }

    public NativeImplementation getNativeImplementation() {
        return nativeImplementation;
    }
}
