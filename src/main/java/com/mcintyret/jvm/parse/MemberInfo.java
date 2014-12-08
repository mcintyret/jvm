package com.mcintyret.jvm.parse;

import java.util.Set;

import com.mcintyret.jvm.parse.attribute.Attributes;

public class MemberInfo {

    private final Set<Modifier> modifiers;

    private final int nameIndex;

    private final int descriptorIndex;

    private final Attributes attributes;

    public MemberInfo(int accessFlags, int nameIndex, int descriptorIndex, Attributes attributes) {
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

    @Override
    public String toString() {
        return "Member Info: name: " + nameIndex + ", descriptor: " + descriptorIndex;
    }
}
