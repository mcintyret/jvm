package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.SimpleType;

public class OopClass extends Oop {

    private final ClassObject classObject;

    public OopClass(ClassObject classObject, Variables fields) {
        super(fields);
        this.classObject = classObject;

        Field[] instanceFields = classObject.getInstanceFields();
        for (int i = 0; i < instanceFields.length; i++) {
            SimpleType type = instanceFields[i].getType().asSimpleType();
            for (int w = 0; w < type.getWidth(); w++) {
                getFields().getTypes()[i + w] = type;
            }
        }
    }

    @Override
    public ClassObject getClassObject() {
        return classObject;
    }
}
