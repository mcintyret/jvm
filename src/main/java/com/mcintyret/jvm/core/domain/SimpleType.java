package com.mcintyret.jvm.core.domain;

public enum SimpleType implements Type {
    BOOLEAN,
    BYTE,
    SHORT,
    CHAR,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    VOID,
    REF;

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
