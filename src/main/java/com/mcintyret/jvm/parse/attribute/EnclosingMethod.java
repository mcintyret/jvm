package com.mcintyret.jvm.parse.attribute;

public class EnclosingMethod extends Attribute {

    private final int classIndex;

    private final int methodIndex;

    protected EnclosingMethod(int length, int classIndex, int methodIndex) {
        super(length);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }
}
