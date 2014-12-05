package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

/**
 * User: tommcintyre
 * Date: 12/5/14
 */
public class Variable {

    public static final Variable NULL = new Variable(SimpleType.REF, Heap.NULL_POINTER);

    public static Variable forOop(Oop oop) {
        return new Variable(SimpleType.REF, oop.getAddress());
    }

    private final SimpleType type;

    private int value;

    public Variable(SimpleType type, int value) {
        this.type = type;
        this.value = value;
    }

    public SimpleType getType() {
        return type;
    }

    public int getCheckedValue(SimpleType type) {
        return assertType(type).value;
    }

    public int getRawValue() {
        return value;
    }

    public Variable withValue(int newValue) {
        return new Variable(type, newValue);
    }

    public <O extends Oop> O getOop() {
        return (O) Heap.getOop(assertType(SimpleType.REF).value);
    }

    // primitive bytecode verification!
    public Variable assertType(SimpleType type) {
        if (this.type != type) {
            throw new IllegalStateException("Trying to treat variable of type " + this.type + " as a " + type);
        }
        return this;
    }
}
