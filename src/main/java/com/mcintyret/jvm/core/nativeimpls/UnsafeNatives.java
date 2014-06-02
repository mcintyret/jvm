package com.mcintyret.jvm.core.nativeimpls;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OperationContext;

import sun.misc.Unsafe;

public enum UnsafeNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // do nothing for now
            return NativeReturn.forVoid();
        }
    },
    ARRAY_BASE_OFFSET("arrayBaseOffset", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(args[0]);
        }
    },
    ARRAY_INDEX_SCALE("arrayIndexScale", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // TODO: do this properly
            return NativeReturn.forInt(4);
        }
    },
    ADDRESS_SIZE("addressSize", "()I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(4);
        }
    },
    COMPARE_AND_SWAP_INT("compareAndSwapInt", "(Ljava/lang/Object;JII)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Oop oop = Heap.getOop(args[1]);
            long offset = Utils.toLong(args[2], args[3]);
            int expect = args[4];
            int update = args[5];

            boolean ret = THE_UNSAFE.compareAndSwapInt(oop.getFields(), byteOffset(offset), expect, update);
            return NativeReturn.forBool(ret);
        }
    };

    private static final Unsafe THE_UNSAFE = getTheUnsafe();

    private static final int BASE = THE_UNSAFE.arrayBaseOffset(int[].class);
    private static final int SHIFT = calculateShift();

    private static long byteOffset(long l) {
        return (l << SHIFT) + BASE;
    }

    private static int calculateShift() {
        int scale = THE_UNSAFE.arrayIndexScale(int[].class);
        if ((scale & (scale - 1)) != 0)
            throw new Error("data type scale not a power of two");
        return 31 - Integer.numberOfLeadingZeros(scale);
    }

    private static Unsafe getTheUnsafe() {
        return AccessController.doPrivileged(
            new PrivilegedAction<Unsafe>() {
                @Override
                public Unsafe run() {
                    try {
                        Field f = Unsafe.class.getDeclaredField("theUnsafe");
                        f.setAccessible(true);
                        return (Unsafe) f.get(null);
                    } catch (NoSuchFieldException e) {
                        // It doesn't matter what we throw;
                        // it's swallowed in getBestComparator().
                        throw new Error();
                    } catch (IllegalAccessException e) {
                        throw new Error(e);
                    }
                }
            });

    }

    private final MethodSignature methodSignature;

    private UnsafeNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "sun/misc/Unsafe";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}