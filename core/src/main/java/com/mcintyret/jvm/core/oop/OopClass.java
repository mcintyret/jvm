package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.util.Utils;

public class OopClass extends Oop {

    private final ClassObject classObject;

    public OopClass(ClassObject classObject, Variables fields) {
        super(fields);
        this.classObject = classObject;

        Utils.setFieldVariablesTypes(classObject.getInstanceFields(), getFields());
    }

    @Override
    public ClassObject getClassObject() {
        return classObject;
    }
}
