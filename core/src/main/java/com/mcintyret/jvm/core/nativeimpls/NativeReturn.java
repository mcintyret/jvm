package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public abstract class NativeReturn {

    private static final NativeReturn FOR_VOID = new NativeReturn() {
        @Override
        public void applyValue(ValueReceiver vr) {
            // Nothing
        }
    };

    private static final NativeReturn FOR_NULL = forReference(Heap.NULL_POINTER);

    private static final NativeReturn FOR_TRUE = forBoolean(true);

    private static final NativeReturn FOR_FALSE = forBoolean(false);

    public static NativeReturn forInt(int i) {
        return new NativeReturn() {
            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveSingleWidth(i, SimpleType.INT);
            }
        };
    }

    public static NativeReturn forBoolean(boolean b) {
        return new NativeReturn() {
            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveSingleWidth(b ? 1 : 0, SimpleType.BOOLEAN);
            }
        };
    }

    public static NativeReturn forReference(Oop oop) {
        if (oop.getAddress() == Oop.UNALLOCATED_ADDRESS) {
            Heap.allocate(oop);
        }
        return forReference(oop.getAddress());
    }

    public static NativeReturn forReference(int address) {
        return new NativeReturn() {
            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveSingleWidth(address, SimpleType.REF);
            }
        };
    }

    public static NativeReturn forLong(long l) {
        return new NativeReturn() {

            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveDoubleWidth(l, SimpleType.LONG);
            }
        };
    }

    public static NativeReturn forDouble(double d) {
        return new NativeReturn() {

            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveDoubleWidth(Double.doubleToLongBits(d), SimpleType.DOUBLE);
            }
        };
    }

    public static NativeReturn forThrowable(Oop throwable) {
        if (throwable.getAddress() == Oop.UNALLOCATED_ADDRESS) {
            Heap.allocate(throwable);
        }
        return new NativeReturn() {
            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveSingleWidth(throwable.getAddress(), SimpleType.REF);
            }

            @Override
            public boolean isThrowable() {
                return true;
            }
        };
    }

    public static NativeReturn forBool(boolean b) {
        return b ? FOR_TRUE : FOR_FALSE;
    }

    public static NativeReturn forNull() {
        return FOR_NULL;
    }

    public static NativeReturn forVoid() {
        return FOR_VOID;
    }

    public abstract void applyValue(ValueReceiver vr);

    public boolean isThrowable() {
        return false;
    }

}
