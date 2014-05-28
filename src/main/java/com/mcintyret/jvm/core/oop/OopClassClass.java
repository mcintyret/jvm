package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.MarkRef;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class OopClassClass extends OopClass {

    private final AbstractClassObject thisClass;

    public OopClassClass(ClassObject classObject, int[] fields, AbstractClassObject thisClass) {
        super(classObject, fields);
        this.thisClass = thisClass;
    }

    public AbstractClassObject getThisClass() {
        return thisClass;
    }

}
