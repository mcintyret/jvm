package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.AttributeType;
import com.mcintyret.jvm.parse.attribute.Attributes;
import com.mcintyret.jvm.parse.attribute.Code;

import java.util.Set;

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


    public Variable[] newEmptyArgArray() {
        Code code = getCode();
        int maxLocals = code == null ? 0 : code.getMaxLocals();

        int argCount = getSignature().getLength();
        int offset = (isStatic() ? 0 : 1);
        Variable[] argArray = new Variable[Math.max(argCount + offset, maxLocals)];

        for (Type type : getSignature().getArgTypes()) {
            for (int w = 0; w < type.getWidth(); w++) {
                argArray[offset++] = new Variable(type.asSimpleType(), 0);
            }
        }

        return argArray;
    }

    // TODO: what about primitive args??
    // TODO: this is fairly inefficient
    public Variable[] newArgArray(Oop... args) {
        Variable[] argArray = newEmptyArgArray();

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                argArray[i] = new Variable(SimpleType.REF, args[i].getAddress());
            }
        }

        return argArray;
    }

}
