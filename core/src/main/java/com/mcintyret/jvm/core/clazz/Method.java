package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.Type;
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


    // TODO: what about primitive args??
    public Variables newArgArray(Oop... args) {
        Code code = getCode();
        int maxLocals = code == null ? 0 : code.getMaxLocals();

        int argCount = getSignature().getLength();
        int offset = (isStatic() ? 0 : 1);
        Variables argArray = new Variables(Math.max(argCount + offset, maxLocals));

        for (Type type : getSignature().getArgTypes()) {
            for (int w = 0; w < type.getWidth(); w++) {
                int val = args.length > w ? args[w].getAddress() : Heap.NULL_POINTER;
                argArray.put(w, type.asSimpleType(), val);
            }
        }

        return argArray;
    }
}
