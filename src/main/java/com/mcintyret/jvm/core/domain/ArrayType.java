package com.mcintyret.jvm.core.domain;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;

import java.util.HashMap;
import java.util.Map;

public final class ArrayType extends ReferenceType {

    private final Type componentType;

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

    private ArrayType(Type componentType, int dimensions) {
        // Type should not be an ArrayType
        this.componentType = componentType;
        this.dimensions = dimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayType arrayType = (ArrayType) o;

        return dimensions == arrayType.dimensions
            && componentType.equals(arrayType.componentType);

    }

    @Override
    public int hashCode() {
        int result = componentType.hashCode();
        result = 31 * result + dimensions;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimensions; i++) {
            sb.append('[');
        }
        return sb.append(componentType).toString();
    }

    public Type getComponentType() {
        return componentType;
    }

    @Override
    public ArrayClassObject getClassObject() {
        return ArrayClassObject.forType(this);
    }
}
