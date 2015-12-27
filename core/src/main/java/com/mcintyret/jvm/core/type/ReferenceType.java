package com.mcintyret.jvm.core.type;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.util.Utils;

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
    public SimpleType asSimpleType() {
        return SimpleType.REF;
    }

    @Override
    public boolean isDoubleWidth() {
        return false;
    }

    public abstract AbstractClassObject getClassObject();

    private OopClassClass oopClassClass;

    @Override
    public synchronized OopClassClass getOopClassClass(boolean gc) {
        if (oopClassClass == null) {
            // TODO: is there a nicer way to do this?
            if (!gc) {
                oopClassClass = Heap.allocateAndGet(
                    Utils.getClassObject(ImportantClasses.JAVA_LANG_CLASS).newObject((clazz, fields) ->
                        new OopClassClass(clazz, fields, this)));

            }
        }
        return oopClassClass;
    }

}
