package com.mcintyret.jvm.parse.attribute.annotation;

public class ConstantElementValue extends ElementValue {

    private final int constantValueIndex;

    protected ConstantElementValue(char tag, int constantValueIndex) {
        super(tag);
        this.constantValueIndex = constantValueIndex;
    }
}
