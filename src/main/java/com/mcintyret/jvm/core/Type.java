package com.mcintyret.jvm.core;

public enum Type {
    BOOLEAN,
    BYTE,
    SHORT,
    CHAR,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    REF;

    public boolean isDoubleWidth() {
        switch (this) {
            case LONG:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    public int getWidth() {
        return isDoubleWidth() ? 2 : 1;
    }

}
