package com.mcintyret.jvm.parse.attribute;

public class ConstantValue extends Attribute {

    private final int valueIndex;

    public ConstantValue(int length, int valueIndex) {
        super(length);
        this.valueIndex = valueIndex;
    }
}
