package com.mcintyret.jvm.parse.cp;

public abstract class CpReference {

    private final int classIndex;

    private final int nameAndTypeIndex;

    public CpReference(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
