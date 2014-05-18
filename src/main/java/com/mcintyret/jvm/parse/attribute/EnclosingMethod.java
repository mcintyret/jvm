package com.mcintyret.jvm.parse.attribute;

public class EnclosingMethod extends Attribute {

    private final int classIndex;

    private final int methodIndex;

    protected EnclosingMethod(int classIndex, int methodIndex) {
        super(AttributeType.ENCLOSING_METHOD);
        this.classIndex = classIndex;
        this.methodIndex = methodIndex;
    }
}
