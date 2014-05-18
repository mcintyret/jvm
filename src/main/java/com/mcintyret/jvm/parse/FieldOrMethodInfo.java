package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.parse.attribute.Attribute;
import java.util.List;
import java.util.Set;

public class FieldOrMethodInfo {

    private final Set<Modifier> modifiers;

    private final int nameIndex;

    private final int descriptorIndex;

    private final List<Attribute> attributes;

    public FieldOrMethodInfo(int accessFlags, int nameIndex, int descriptorIndex, List<Attribute> attributes) {
        this.modifiers = Modifier.translate(accessFlags);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }
}
