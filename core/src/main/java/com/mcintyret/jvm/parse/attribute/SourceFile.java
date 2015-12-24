package com.mcintyret.jvm.parse.attribute;

public class SourceFile extends Attribute {

    private final int sourcefileIndex;

    SourceFile(int sourcefileIndex) {
        super(AttributeType.SOURCE_FILE);
        this.sourcefileIndex = sourcefileIndex;
    }
}
