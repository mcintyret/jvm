package com.mcintyret.jvm.parse.cp;

public class NameAndType {

    private final int nameIndex;

    private final int descriptorIndex;

    public NameAndType(int descriptorIndex, int nameIndex) {
        this.descriptorIndex = descriptorIndex;
        this.nameIndex = nameIndex;
    }
}
