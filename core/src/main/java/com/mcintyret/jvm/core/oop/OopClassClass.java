package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.Type;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
// An Oop that corresponds to a Class object
public class OopClassClass extends OopClass {

    private final Type thisType;

    public OopClassClass(ClassObject classObject, Variables fields, Type thisType) {
        super(classObject, fields);
        this.thisType = thisType;
    }

    public Type getThisType() {
        return thisType;
    }

}
