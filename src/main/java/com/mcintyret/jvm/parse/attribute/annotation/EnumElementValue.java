package com.mcintyret.jvm.parse.attribute.annotation;

public class EnumElementValue extends ElementValue {

    private final int enumTypeNameIndex;

    private final int constantNameIndex;

    protected EnumElementValue(char tag, int enumTypeNameIndex, int constantNameIndex) {
        super(tag);
        this.enumTypeNameIndex = enumTypeNameIndex;
        this.constantNameIndex = constantNameIndex;
    }
}
