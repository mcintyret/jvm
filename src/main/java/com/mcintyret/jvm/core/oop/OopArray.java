package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;

public class OopArray extends Oop {

    private final ArrayClassObject arrayClassObject;

    private final int width;

    public OopArray(ArrayClassObject classObject, int[] fields) {
        super(fields);
        this.arrayClassObject = classObject;
        this.width = classObject.getType().getComponentType().getSimpleType().getWidth();
    }

    public int getLength() {
        return getFields().length / width;
    }

    @Override
    public ArrayClassObject getClassObject() {
        return arrayClassObject;
    }
}
