package com.mcintyret.jvm.parse.cp;

public class FieldRef extends Reference {
    public FieldRef(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }
}
