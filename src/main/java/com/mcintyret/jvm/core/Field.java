package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.Type;

public class Field {

    private final Type type;

    private final int offset;

    public Field(Type type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
}
