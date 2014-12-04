package com.mcintyret.jvm.core.domain;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.oop.OopClassClass;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
public abstract class ReferenceType implements Type {

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isDoubleWidth() {
        return false;
    }

    public abstract AbstractClassObject getClassObject();

    private OopClassClass oopClassClass;

    @Override
    public OopClassClass getOopClassClass() {
        return oopClassClass == null ? (oopClassClass = Heap.allocateAndGet(
            Utils.getClassObject(ImportantClasses.JAVA_LANG_CLASS).newObject((clazz, fields) ->
                new OopClassClass(clazz, fields, this)))) : oopClassClass;
    }

}
