package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

public class Variables {

    public static final Variables EMPTY_VARIABLES = new Variables(0);

    // TODO: can be smarter about this - ie only from non-library code, only the first time it is executed etc.
    private static boolean checking = false;

    private final int[] values;
    private final SimpleType[] types;

    public static Variables copyInto(Variables source, Variables target) {
        System.arraycopy(source.values, 0, target.values, 0, source.length());
        System.arraycopy(source.types, 0, target.types, 0, source.length());
        return target;
    }

    public Variables(int size) {
        this.values = new int[size];
        this.types = new SimpleType[size];
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

    public void putBoolean(int i, boolean value) {
        put(i, SimpleType.BOOLEAN, value ? 1 : 0);
    }

    public void put(int i, SimpleType type, int value) {
        types[i] = type;
        values[i] = value;
    }

    public void putWide(int i, SimpleType type, long value) {
        putWide(i, type, (int) (value >> 32), (int) value);
    }

    public void putWide(int i, SimpleType type, int left, int right) {
        if (type == SimpleType.REF) {
            throw new IllegalStateException();
        }
        types[i] = type;
        types[i + 1] = type;

        values[i] = left;
        values[i + 1] = right;
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
        return getRawValue(i);
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

    // TODO: how is this used?
    void clear(int i) {
        values[i] = 0;
        types[i] = null;
    }

    public Variables copy(int newSize) {
        return copyInto(this, new Variables(newSize));
    }
}
