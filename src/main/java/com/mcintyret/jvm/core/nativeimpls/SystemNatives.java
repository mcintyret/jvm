package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.load.ClassLoader;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum SystemNatives implements NativeImplementation {
    ARRAY_COPY("arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V") {
        @Override
        public NativeReturn execute(int[] args) {
            OopArray src = Heap.getOopArray(args[0]);
            OopArray dest = Heap.getOopArray(args[2]);

            Type srcType = componentType(src);
            Type destType = componentType(dest);

            if (srcType.isPrimitive() && srcType != destType) {
                throw new ArrayStoreException("Type mismatch: " + srcType + " and " + destType);
            } else if (!srcType.isPrimitive()) {
                // TODO: move isInstanceOf around - might need to be a Type property?
            }

            int width = srcType.getSimpleType().getWidth();

            int srcPos = args[1] * width;
            int destPos = args[3] * width;
            int len = args[4] * width;

            System.arraycopy(src.getFields(), srcPos, dest.getFields(), destPos, len);

            return NativeReturn.forVoid();
        }
    },
    CURRENT_TIME_MILLIS("currentTimeMillis", "()J") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forLong(System.currentTimeMillis());
        }
    },
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            // Good place to set StdOut
            ClassObject fileDescriptor = CLASS_LOADER.getClassObject("java/io/FileDescriptor");
            OopClass outFd = Heap.getOopClass(fileDescriptor.getStaticFieldValues()[1]);

            ClassObject fileOutputStream = CLASS_LOADER.getClassObject("java/io/FileOutputStream");
            OopClass fos = fileOutputStream.newObject();
            Heap.allocate(fos);

            OopClass lockObj = CLASS_LOADER.getClassObject("java/lang/Object").newObject();
            Heap.allocate(lockObj);

            fos.getFields()[0] = outFd.getAddress();
            fos.getFields()[4] = lockObj.getAddress();






            // Already done
            return NativeReturn.forVoid();
        }
    },
    NANO_TIME("nanoTime", "()J") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forLong(System.nanoTime());
        }
    },
    IDENTITY_HASH_CODE("identityHashCode", "(Ljava/lang/Object;)I") {
        @Override
        public NativeReturn execute(int[] args) {
            return ObjectNatives.HASHCODE.execute(args);
        }
    };

    private static final ClassLoader CLASS_LOADER = ClassLoader.DEFAULT_CLASSLOADER;

    private final MethodSignature methodSignature;

    private SystemNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/System";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    private static Type componentType(OopArray array) {
        return array.getClassObject().getType().getComponentType();
    }
}