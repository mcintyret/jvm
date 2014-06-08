package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.AttributeType;
import com.mcintyret.jvm.parse.attribute.Attributes;
import com.mcintyret.jvm.parse.attribute.Code;

public class Method extends Member {

    public static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private final MethodSignature signature;

    public Method(Set<Modifier> modifiers, Attributes attributes, MethodSignature signature, int offset) {
        super(modifiers, attributes, offset);
        this.signature = signature;
    }

    public MethodSignature getSignature() {
        return signature;
    }

    public Code getCode() {
        return (Code) getAttributes().getAttribute(AttributeType.CODE);
    }

    @Override
    public String toString() {
        return getClassObject().getClassName() + "." + signature;
    }

    public int[] newArgArray() {
        Code code = getCode();
        int maxLocals = code == null ? 0 : code.getMaxLocals();

        int args = getSignature().getLength();
        return new int[Math.max(args + (isStatic() ? 0 : 1), maxLocals)];
    }

}
