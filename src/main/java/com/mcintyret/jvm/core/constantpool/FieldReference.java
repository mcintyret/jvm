package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Field;

public class FieldReference extends AbstractReference {

    public FieldReference(ClassObject classObject, int methodIndex) {
        super(classObject, methodIndex);
    }

    public Field getInstanceField() {
        return getClassObject().getInstanceFields()[getIndex()];
    }

    public Field getStaticField() {
        return getClassObject().getStaticFields()[getIndex()];
    }

}
