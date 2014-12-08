package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.type.SimpleType;

/**
 * User: tommcintyre
 * Date: 12/5/14
 */
public class Variable {

    public static final Variable NULL = new Variable(SimpleType.REF, Heap.NULL_POINTER);

    private final SimpleType type;

    private int value;

    public Variable(SimpleType type, int value) {
        this.type = type;
        this.value = value;
    }

    public SimpleType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
