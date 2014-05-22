package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.Set;

public abstract class Member {

    private ClassObject classObject;

    private final Set<Modifier> modifiers;

    private final Attributes attributes;

    private boolean isStatic;

    protected Member(Set<Modifier> modifiers, Attributes attributes) {
        this.modifiers = modifiers;
        this.attributes = attributes;
        this.isStatic = hasModifier(Modifier.STATIC);
    }

    public ClassObject getClassObject() {
        return classObject;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public boolean isStatic() {
        return isStatic;
    }

    void setClassObject(ClassObject classObject) {
        this.classObject = classObject;
    }
}
