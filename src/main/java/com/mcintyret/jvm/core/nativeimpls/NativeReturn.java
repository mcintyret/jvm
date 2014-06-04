package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;

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

    private static final NativeReturn FOR_NULL = forInt(Heap.NULL_POINTER);

    private static final NativeReturn FOR_TRUE = forInt(1);

    private static final NativeReturn FOR_FALSE = forInt(0);

    public static NativeReturn forInt(int i) {
        return new NativeReturn() {
            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveInt(i);
            }
        };
    }

    public static NativeReturn forReference(Oop oop) {
        if (oop.getAddress() == Oop.UNALLOCATED_ADDRESS) {
            Heap.allocate(oop);
        }
        return forInt(oop.getAddress());
    }

    public static NativeReturn forLong(long l) {
        return new NativeReturn() {

            @Override
            public void applyValue(ValueReceiver vr) {
                vr.receiveLong(l);
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
                vr.receiveInt(throwable.getAddress());
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
