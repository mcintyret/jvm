package com.mcintyret.jvm.core.domain;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopPrimitiveClass;
import com.mcintyret.jvm.load.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.mcintyret.jvm.load.ClassLoader.*;

public enum SimpleType implements Type {
    BOOLEAN("Z", 4),
    BYTE("B", 8),
    SHORT("S", 9),
    CHAR("C", 5),
    INT("I", 10),
    LONG("J", 11),
    FLOAT("F", 6),
    DOUBLE("D", 7),
    VOID("V", -1),
    REF("L", -1);

    private static final Map<Character, SimpleType> CHAR_MAP = makeCharMap();

    private static final Map<Byte, SimpleType> BYTE_MAP = makeByteMap();

    private static Map<Character, SimpleType> makeCharMap() {
        Map<Character, SimpleType> map = new HashMap<>();
        for (SimpleType st : values()) {
            map.put(st.str.charAt(0), st);
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map<Byte, SimpleType> makeByteMap() {
        Map<Byte, SimpleType> map = new HashMap<>();
        for (SimpleType st : values()) {
            map.put(st.b, st);
        }
        return Collections.unmodifiableMap(map);
    }

    private final String str;

    private final byte b;

    private OopPrimitiveClass classOop;

    private SimpleType(String str, int i) {
        this.str = str;
        this.b = (byte) i;
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
        return CHAR_MAP.get(ch);
    }

    public static SimpleType forByte(byte b) {
        return BYTE_MAP.get(b);
    }

    public boolean isPrimitive() {
        switch (this) {
            case REF:
                return false;
            case VOID:
                throw new UnsupportedOperationException();
            default:
                return true;

        }
    }

    @Override
    public OopPrimitiveClass getClassOop() {
        return classOop == null ? (classOop = Heap.allocateAndGet(CLASS_CLASS.newObject((clazz, fields) ->
                new OopPrimitiveClass(clazz, fields, SimpleType.this)))) : classOop;
    }
}
