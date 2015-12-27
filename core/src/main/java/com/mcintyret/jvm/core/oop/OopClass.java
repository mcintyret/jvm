package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.Variables;

public class OopClass extends Oop {

    private final ClassObject classObject;

    public OopClass(ClassObject classObject, Variables fields) {
        super(fields);
        this.classObject = classObject;

        Field[] instanceFields = classObject.getInstanceFields();
        for (int i = 0; i < instanceFields.length; i++) {
            getFields().getTypes()[i] = instanceFields[i].getType().asSimpleType();
        }
    }

    @Override
    public ClassObject getClassObject() {
        return classObject;
    }
}
