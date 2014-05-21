package com.mcintyret.jvm.core.domain;

import java.util.HashMap;
import java.util.Map;

public final class ReferenceType implements Type {

    private static final Map<String, ReferenceType> CACHE = new HashMap<>();

    public static ReferenceType forClass(String className) {
        ReferenceType ret = CACHE.get(className);
        if (ret == null) {
            ret = new ReferenceType(className);
            CACHE.put(className, ret);
        }
        return ret;
    }

    private final String className;

    private ReferenceType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public SimpleType getSimpleType() {
        return SimpleType.REF;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceType that = (ReferenceType) o;

        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return "L" + className + ";";
    }
}
