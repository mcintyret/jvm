package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.Oop;
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

    public int[] newArgArray(Oop... args) {
        Code code = getCode();
        int maxLocals = code == null ? 0 : code.getMaxLocals();

        int argCount = getSignature().getLength();
        int[] argArray = new int[Math.max(argCount + (isStatic() ? 0 : 1), maxLocals)];

        for (int i = 0; i < args.length; i++) {
            argArray[i] = args[i].getAddress();
        }

        return argArray;
    }

}
