package com.mcintyret.jvm.parse.attribute;

public class ConstantValue extends Attribute {

    private final int valueIndex;

    public ConstantValue(int valueIndex) {
        super(AttributeType.CONSTANT_VALUE);
        this.valueIndex = valueIndex;
    }
}
