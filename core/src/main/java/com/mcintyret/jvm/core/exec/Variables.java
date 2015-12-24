package com.mcintyret.jvm.core.exec;

import java.util.Arrays;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

public class Variables {

    // TODO: can be smarter about this - ie only from non-library code, only the first time it is executed etc.
    private static boolean checking = false;

    private final Variable[] variables;

    public Variables(int size) {
        this.variables = new Variable[size];
    }

    // Copy constructor
    private Variables(Variable[] variables) {
        this.variables = variables;
    }

    public boolean isEmpty(int i) {
        return variables[i] == null;
    }

    public <O extends Oop> O getOop(int i) {
        return (O) Heap.getOop(getCheckedValue(i, SimpleType.REF));
    }

    public int getInt(int i) {
        return getCheckedValue(i, SimpleType.INT);
    }

    public float getFloat(int i) {
        return Float.intBitsToFloat(getCheckedValue(i, SimpleType.FLOAT));
    }

    public long getLong(int i) {
        return getLongInternal(i, SimpleType.LONG);
    }

    public double getDouble(int i) {
        return Double.longBitsToDouble(getLongInternal(i, SimpleType.DOUBLE));
    }

    public boolean getBoolean(int i) {
        return getCheckedValue(i, SimpleType.BOOLEAN) > 0;
    }

    public void putOop(int i, Oop val) {
        variables[i] = Variable.forOop(val);
    }

    public void putNull(int i) {
        put(i, SimpleType.REF, Heap.NULL_POINTER);
    }

    public void putInt(int i, int val) {
        put(i, SimpleType.INT, val);
    }

    public void putFloat(int i, float val) {
        put(i, SimpleType.FLOAT, Float.floatToIntBits(val));
    }

    public void put(int i, SimpleType type, int value) {
        variables[i] = Variable.forType(type, value);
    }

    public void putWide(int i, SimpleType type, long value) {
        putWide(i, new WideVariable(type, value));
    }

    public void put(int i, Variable v) {
        put(i, v.getType(), v.getValue());
    }

    public void putWide(int i, WideVariable v) {
        put(i, v.getType(), v.getLeft());
        put(i + 1, v.getType(), v.getRight());
    }

    private SimpleType checkType(int i, SimpleType type) {
        SimpleType currentType = variables[i].getType();
        if (currentType != null && currentType != type) {
//            throw new IllegalStateException("Expected value of type " + type + " but was of type " + types[i]); // this may be completely invalid!
        }
        return type;
    }

    private long getLongInternal(int i, SimpleType type) {
        return Utils.toLong(
            getCheckedValue(i, type),
            getCheckedValue(i + 1, type)
        );
    }

    public int getCheckedValue(int i, SimpleType type) {
        Variable v = variables[i];
        if (checking) {
            // This condition will probably need to be made smarter to deal with automatic widening conversions, which are allowed
            if (v.getType() != type) {
                throw new IllegalStateException("Expected value of type " + type + " but was of type " + v.getType());
            }
        }
        return v.getValue();
    }

    public int getRawValue(int i) {
        return variables[i].getValue();
    }

    public int[] getRawValues() {
        int[] vals = new int[variables.length];
        for (int i = 0; i < vals.length; i++) {
            vals[i] = variables[i].getValue();
        }
        return vals;
    }

    public SimpleType[] getTypes() {
        return Utils.transformArray(variables, Variable::getType, SimpleType.class);
    }

    public SimpleType getType(int i) {
        return variables[i].getType();
    }

    public int length() {
        return variables.length;
    }

    public Variable get(int i) {
        return variables[i];
    }

    public WideVariable getWide(int i) {
        Variable v1 = variables[i];
        Variable v2 = variables[i + 1];
        return new WideVariable(v1.getType(), Utils.toLong(v1.getValue(), v2.getValue()));
    }

    void clear(int i) {
        variables[i] = null;
    }

    public Variables copy(int newSize) {
        return new Variables(Arrays.copyOf(variables, newSize));
    }
}
