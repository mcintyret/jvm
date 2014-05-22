package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.Set;

public class Field extends Member {

    private final String name;

    private final Type type;

    private final int offset;

    public Field(Set<Modifier> modifiers, Attributes attributes, String name, Type type, int offset) {
        super(modifiers, attributes);
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }
}
