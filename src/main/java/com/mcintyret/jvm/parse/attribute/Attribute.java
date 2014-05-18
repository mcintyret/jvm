package com.mcintyret.jvm.parse.attribute;

public abstract class Attribute {

    private final int length;

    protected Attribute(int length) {
        this.length = length;
    }
}
