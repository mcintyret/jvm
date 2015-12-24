package com.mcintyret.jvm.parse.attribute;

public abstract class Attribute {

    private final AttributeType type;

    protected Attribute(AttributeType type) {
        this.type = type;
    }

    public AttributeType getType() {
        return type;
    }
}
