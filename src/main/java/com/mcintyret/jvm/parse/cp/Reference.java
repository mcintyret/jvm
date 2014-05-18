package com.mcintyret.jvm.parse.cp;

abstract class Reference {

    private final int classIndex;

    private final int nameAndTypeIndex;

    public Reference(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}
