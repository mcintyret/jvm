package com.mcintyret.jvm.core.domain;

public enum SimpleType implements Type {
    BOOLEAN("Z"),
    BYTE("B"),
    SHORT("S"),
    CHAR("C"),
    INTEGER("I"),
    LONG("J"),
    FLOAT("F"),
    DOUBLE("D"),
    VOID("V"),
    REF("Probably shouldn't see this");

    private final String str;

    private SimpleType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public boolean isDoubleWidth() {
        switch (this) {
            case LONG:
            case DOUBLE:
                return true;
            case VOID:
                throw new UnsupportedOperationException();
            default:
                return false;
        }
    }

    public int getWidth() {
        return isDoubleWidth() ? 2 : 1;
    }

    @Override
    public SimpleType getSimpleType() {
        return this;
    }
}
