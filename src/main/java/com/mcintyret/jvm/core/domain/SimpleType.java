package com.mcintyret.jvm.core.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    private static final Map<Character, SimpleType> MAP = makeMap();

    private static Map<Character, SimpleType> makeMap() {
        Map<Character, SimpleType> map = new HashMap<>();
        for (SimpleType st : values()) {
            map.put(st.str.charAt(0), st);
        }
        return Collections.unmodifiableMap(map);
    }

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

    public static SimpleType forChar(char ch) {
        return MAP.get(ch);
    }

    public static SimpleType forByte(byte b) {
        return forChar((char) b);
    }

    public static SimpleType forString(String str) {
        return forChar(str.charAt(0));
    }
}
