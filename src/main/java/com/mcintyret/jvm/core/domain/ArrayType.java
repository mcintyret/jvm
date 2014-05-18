package com.mcintyret.jvm.core.domain;

import java.util.HashMap;
import java.util.Map;

public final class ArrayType implements Type {

    private final Type type;

    private final int dimensions;

    private static final Map<ArrayType, ArrayType> CACHE = new HashMap<>();

    public static ArrayType create(Type type, int dimensions) {
        ArrayType at = new ArrayType(type, dimensions);
        ArrayType canonical = CACHE.get(at);
        if (canonical == null) {
            canonical = at;
            CACHE.put(at, at);
        }
        return canonical;
    }

    private ArrayType(Type type, int dimensions) {
        // Type should not be an ArrayType
        this.type = type;
        this.dimensions = dimensions;
    }

    @Override
    public SimpleType getSimpleType() {
        return SimpleType.REF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayType arrayType = (ArrayType) o;

        return dimensions == arrayType.dimensions
            && type.equals(arrayType.type);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + dimensions;
        return result;
    }
}
