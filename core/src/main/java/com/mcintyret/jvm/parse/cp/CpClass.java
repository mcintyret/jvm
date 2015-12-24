package com.mcintyret.jvm.parse.cp;

public class CpClass {

    private final int nameIndex;

    public CpClass(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }
}
