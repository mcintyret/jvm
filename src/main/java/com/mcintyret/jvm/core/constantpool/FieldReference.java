package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.Field;

public class FieldReference extends AbstractReference {

    public FieldReference(ClassObject classObject, int fieldIndex) {
        super(classObject, fieldIndex);
    }

    public Field getInstanceField() {
        return getClassObject().getInstanceFields()[getIndex()];
    }

    public Field getStaticField() {
        return getClassObject().getStaticFields()[getIndex()];
    }

}
