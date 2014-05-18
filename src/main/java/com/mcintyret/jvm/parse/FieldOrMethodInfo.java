package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.parse.attribute.Attribute;
import java.util.List;

public class FieldOrMethodInfo {

    private final int accessFlags;

    private final int nameIndex;

    private final int descriptorIndex;

    private final List<Attribute> attributes;

    public FieldOrMethodInfo(int accessFlags, int nameIndex, int descriptorIndex, List<Attribute> attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }
}
