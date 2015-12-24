package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

public class Variables {

    // TODO: can be smarter about this - ie only from non-library code, only the first time it is executed etc.
    private static boolean checking = false;

    private final int[] values;
    private final SimpleType[] types;

    public Variables(int size) {
        this.values = new int[size];
        this.types = new SimpleType[size];
    }

    public boolean isEmpty(int i) {
        return types[i] == null;
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
        put(i, SimpleType.REF, val.getAddress());
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
        types[i] = checkType(i, type);
        values[i] = value;
    }

    public void put(int i, Variable v) {
        put(i, v.getType(), v.getValue());
    }

    public void putWide(int i, WideVariable v) {
        put(i, v.getType(), v.getLeft());
        put(i + 1, v.getType(), v.getRight());
    }

    private SimpleType checkType(int i, SimpleType type) {
        SimpleType currentType = types[i];
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
        if (checking) {
            // This condition will probably need to be made smarter to deal with automatic widening conversions, which are allowed
            if (types[i] != type) {
                throw new IllegalStateException("Expected value of type " + type + " but was of type " + types[i]);
            }
        }
        return values[i];
    }

    public int getRawValue(int i) {
        return values[i];
    }

    public int[] getRawValues() {
        return values;
    }

    public SimpleType[] getTypes() {
        return types;
    }

    public SimpleType getType(int i) {
        return types[i];
    }

    public int length() {
        return values.length;
    }

    public Variable get(int i) {
        return new Variable(types[i], values[i]);
    }

    public WideVariable getWide(int i) {
        return new WideVariable(types[i], Utils.toLong(values[i], values[i + 1]));
    }

    void clear(int i) {
        types[i] = null;
        values[i] = 0;
    }

}
