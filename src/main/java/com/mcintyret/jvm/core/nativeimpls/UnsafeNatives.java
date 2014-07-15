package com.mcintyret.jvm.core.nativeimpls;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;

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
    OBJECT_FIELD_OFFSET("objectFieldOffset", "(Ljava/lang/reflect/Field;)J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject fieldClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Field");
            OopClass field = Heap.getOopClass(args[1]);

            OopClassClass declaringClassOop = (OopClassClass) fieldClass.findField("clazz", false).getOop(field);

            boolean isStatic = Modifier.translate(fieldClass.findField("modifiers", false).getInt(field)).contains(Modifier.STATIC);

            ClassObject declaringClass = (ClassObject) declaringClassOop.getThisClass();

            String fieldName = Utils.toString((OopClass) fieldClass.findField("name", false).getOop(field));

            return NativeReturn.forLong(declaringClass.findField(fieldName, isStatic).getOffset());
        }
    },
    ADDRESS_SIZE("addressSize", "()I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(4);
        }
    },
    ALLOCATE_MEMORY("allocateMemory", "(J)J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forLong(THE_UNSAFE.allocateMemory(Utils.toLong(args[1], args[2])));
        }
    },
    FREE_MEMORY("freeMemory", "(J)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.freeMemory(Utils.toLong(args[1], args[2]));
            return NativeReturn.forVoid();
        }
    },
    PUT_LONG("putLong", "(JJ)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.putLong(Utils.toLong(args[1], args[2]), Utils.toLong(args[3], args[4]));
            return NativeReturn.forVoid();
        }
    },
    PUT_ORDERED_OBJECT("putOrderedObject", "(Ljava/lang/Object;JLjava/lang/Object;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.putOrderedObject(Heap.getOop(args[1]).getFields(), Utils.toLong(args[2], args[3]), Heap.getOop(args[4]).getFields());
            return NativeReturn.forVoid();
        }
    },
    GET_BYTE("getByte", "(J)B") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(THE_UNSAFE.getByte(Utils.toLong(args[1], args[2])));
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