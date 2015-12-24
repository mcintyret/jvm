package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;

public abstract class Member {

    private ClassObject classObject;

    private final Set<Modifier> modifiers;

    private final Attributes attributes;

    private boolean isStatic;

    private final int offset;

    protected Member(Set<Modifier> modifiers, Attributes attributes, int offset) {
        this.modifiers = modifiers;
        this.attributes = attributes;
        this.isStatic = hasModifier(Modifier.STATIC);
        this.offset = offset;
    }

    public ClassObject getClassObject() {
        return classObject;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setClassObject(ClassObject classObject) {
        this.classObject = classObject;
    }

    public int getOffset() {
        return offset;
    }
}
