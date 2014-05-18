package com.mcintyret.jvm.parse.attribute;

public class SourceFile extends Attribute {

    private final int sourcefileIndex;

    SourceFile(int length, int sourcefileIndex) {
        super(length);
        this.sourcefileIndex = sourcefileIndex;
    }
}
