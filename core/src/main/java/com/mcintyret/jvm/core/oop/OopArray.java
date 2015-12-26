package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.exec.Variables;

public class OopArray extends Oop {

    private final ArrayClassObject arrayClassObject;

    private final int width;

    public OopArray(ArrayClassObject classObject, Variables fields) {
        super(fields);
        this.arrayClassObject = classObject;
        this.width = classObject.getType().getComponentType().getWidth();
    }

    public int getLength() {
        return getFields().length() / width;
    }

    @Override
    public ArrayClassObject getClassObject() {
        return arrayClassObject;
    }
}
