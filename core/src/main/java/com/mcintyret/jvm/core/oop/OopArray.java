package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.exec.Variables;

import java.util.Arrays;

public class OopArray extends Oop {

    private final ArrayClassObject arrayClassObject;

    private final int width;

    public OopArray(ArrayClassObject classObject, Variables fields) {
        super(fields);
        this.arrayClassObject = classObject;
        this.width = classObject.getType().getComponentType().getWidth();

        Arrays.fill(getFields().getTypes(), arrayClassObject.getType().getComponentType().asSimpleType());
    }

    public int getLength() {
        return getFields().length() / width;
    }

    @Override
    public ArrayClassObject getClassObject() {
        return arrayClassObject;
    }
}
