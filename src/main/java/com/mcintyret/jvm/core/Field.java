package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.Type;

public class Field {

    private final Type type;

    private final String name;

    private final int offset;

    public Field(Type type, String name, int offset) {
        this.type = type;
        this.name = name;
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
