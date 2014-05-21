package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.MarkRef;
import com.mcintyret.jvm.core.clazz.ClassObject;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class OopClassClass extends OopClass {

    private final ClassObject thisClass;

    public OopClassClass(ClassObject classObject, MarkRef markRef, int[] fields, ClassObject thisClass) {
        super(classObject, markRef, fields);
        this.thisClass = thisClass;
    }

    public ClassObject getThisClass() {
        return thisClass;
    }

}
