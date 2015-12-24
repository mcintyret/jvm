package com.mcintyret.jvm.parse.attribute.annotation;

public class ClassElementValue extends ElementValue {

    private final int classInfoIndex;

    protected ClassElementValue(char tag, int classInfoIndex) {
        super(tag);
        this.classInfoIndex = classInfoIndex;
    }
}
