package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.Set;

public class FieldOrMethodInfo {

    private final Set<Modifier> modifiers;

    private final int nameIndex;

    private final int descriptorIndex;

    private final Attributes attributes;

    public FieldOrMethodInfo(int accessFlags, int nameIndex, int descriptorIndex, Attributes attributes) {
        this.modifiers = Modifier.translate(accessFlags);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

}
