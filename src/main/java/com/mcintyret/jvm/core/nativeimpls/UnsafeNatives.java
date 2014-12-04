package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.thread.Threads;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

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
            return NativeReturn.forInt(0);
        }
    },
    ARRAY_INDEX_SCALE("arrayIndexScale", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
//            OopClassClass occ = (OopClassClass) Heap.getOop(args[1]);
//            ArrayClassObject aco = (ArrayClassObject) occ.getThisType();

//            return NativeReturn.forInt(aco.getType().getComponentType().getSimpleType().getWidth());
            return NativeReturn.forInt(1); // We'll do the math!
        }
    },
    OBJECT_FIELD_OFFSET("objectFieldOffset", "(Ljava/lang/reflect/Field;)J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject fieldClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Field");
            OopClass field = Heap.getOopClass(args[1]);

            OopClassClass declaringClassOop = (OopClassClass) fieldClass.findField("clazz", false).getOop(field);

            boolean isStatic = Modifier.translate(fieldClass.findField("modifiers", false).getInt(field)).contains(Modifier.STATIC);

            ClassObject declaringClass = ((NonArrayType) declaringClassOop.getThisType()).getClassObject();

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
    },
    COMPARE_AND_SWAP_LONG("compareAndSwapLong", "(Ljava/lang/Object;JJJ)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Oop oop = Heap.getOop(args[1]);
            long offset = Utils.toLong(args[2], args[3]);

            /*
             Note that these arguments to toLong() look like they are inverted. This is because UNSAFE expects the
             two 32-bit sections of a long to be in this order.
              */
            long expect = Utils.toLong(args[5], args[4]);
            long update = Utils.toLong(args[7], args[6]);

            boolean ret = THE_UNSAFE.compareAndSwapLong(oop.getFields(), byteOffset(offset), expect, update);
            return NativeReturn.forBool(ret);
        }
    },
    COMPARE_AND_SWAP_OBJECT("compareAndSwapObject", "(Ljava/lang/Object;JLjava/lang/Object;Ljava/lang/Object;)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return COMPARE_AND_SWAP_INT.execute(args, ctx);
        }
    },
    FREE_MEMORY("freeMemory", "(J)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.freeMemory(Utils.toLong(args[1], args[2]));
            return NativeReturn.forVoid();
        }
    },
    GET_BYTE("getByte", "(J)B") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(THE_UNSAFE.getByte(Utils.toLong(args[1], args[2])));
        }
    },
    GET_INT("getInt", "(Ljava/lang/Object;J)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            long offset = Utils.toLong(args[2], args[3]);
            int[] fields = Heap.getOop(args[1]).getFields();
            return NativeReturn.forInt(THE_UNSAFE.getInt(fields, byteOffset(offset)));
        }
    },
    GET_INT_VOLATILE("getIntVolatile", "(Ljava/lang/Object;J)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            long offset = Utils.toLong(args[2], args[3]);
            int[] fields = Heap.getOop(args[1]).getFields();
            return NativeReturn.forInt(THE_UNSAFE.getIntVolatile(fields, byteOffset(offset)));
        }
    },
    GET_OBJECT("getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return GET_INT.execute(args, ctx);
        }
    },
    GET_OBJECT_VOLATILE("getObjectVolatile", "(Ljava/lang/Object;J)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return GET_INT_VOLATILE.execute(args, ctx);
        }
    },
    PUT_INT("putInt", "(Ljava/lang/Object;JI)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            long offset = Utils.toLong(args[2], args[3]);
            int[] fields = Heap.getOop(args[1]).getFields();

            THE_UNSAFE.putInt(fields, byteOffset(offset), args[4]);
            return NativeReturn.forVoid();
        }
    },
    PUT_INT_VOLATILE("putIntVolatile", "(Ljava/lang/Object;JI)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            long offset = Utils.toLong(args[2], args[3]);
            int[] fields = Heap.getOop(args[1]).getFields();

            THE_UNSAFE.putIntVolatile(fields, byteOffset(offset), args[4]);
            return NativeReturn.forVoid();
        }
    },
    PUT_ORDERED_INT("putOrderedInt", "(Ljava/lang/Object;JI)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            long offset = Utils.toLong(args[2], args[3]);
            int[] fields = Heap.getOop(args[1]).getFields();

            THE_UNSAFE.putOrderedInt(fields, byteOffset(offset), args[4]);
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
    PUT_OBJECT("putObject", "(Ljava/lang/Object;JLjava/lang/Object;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return PUT_INT.execute(args, ctx);
        }
    },
    PUT_OBJECT_VOLATILE("putObjectVolatile", "(Ljava/lang/Object;JLjava/lang/Object;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return PUT_INT_VOLATILE.execute(args, ctx);
        }
    },
    PUT_ORDERED_OBJECT("putOrderedObject", "(Ljava/lang/Object;JLjava/lang/Object;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return PUT_ORDERED_INT.execute(args, ctx);
        }
    },
    PARK("park", "(ZJ)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.park(args[1] > 0, Utils.toLong(args[2], args[3]));
            return NativeReturn.forVoid();
        }
    },
    UNPARK("unpark", "(Ljava/lang/Object;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            THE_UNSAFE.unpark(Threads.get(Heap.getOopClass(args[1])).getThread());
            return NativeReturn.forVoid();
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