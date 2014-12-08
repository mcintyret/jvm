package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.type.SimpleType;

public class WideVariable {

    private final SimpleType type;

    private final long value;

    public WideVariable(SimpleType type, long value) {
        this.type = type;
        this.value = value;
    }

    public SimpleType getType() {
        return type;
    }

    public long getValue() {
        return value;
    }


    // TODO: get methods like this in one place
    public int getLeft() {
        return (int) (value >> 32);
    }

    public int getRight() {
        return (int) value;
    }
}
