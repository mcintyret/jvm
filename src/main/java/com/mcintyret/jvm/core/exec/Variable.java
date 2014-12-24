package com.mcintyret.jvm.core.exec;

import java.util.function.IntSupplier;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

/**
 * User: tommcintyre
 * Date: 12/5/14
 */
public final class Variable {

    private static final Variable NULL = new Variable(SimpleType.REF, () -> Heap.NULL_POINTER);

    public static Variable forNull() {
        return NULL;
    }

    public static Variable forType(SimpleType type, int value) {
        return new Variable(type, () -> value);
    }

    public static Variable forInt(int i) {
        return forType(SimpleType.INT, i);
    }

    public static Variable forFloat(float f) {
        return forType(SimpleType.FLOAT, Float.floatToIntBits(f));
    }

    public static Variable forBool(boolean b) {
        return forType(SimpleType.BOOLEAN, b ? 1 : 0);
    }

    public static Variable forOop(Oop oop) {
        return new Variable(SimpleType.REF, oop::getAddress);
    }


    private final SimpleType type;

    private final IntSupplier valueSupplier;

    private Variable(SimpleType type, IntSupplier valueSupplier) {
        this.type = type;
        this.valueSupplier = valueSupplier;
    }

    public final SimpleType getType() {
        return type;
    }

    public final int getValue() {
        return valueSupplier.getAsInt();
    }
}
