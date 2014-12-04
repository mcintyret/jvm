package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.load.ClassLoader;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum SystemNatives implements NativeImplementation {
    ARRAY_COPY("arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopArray src = Heap.getOopArray(args[0]);
            OopArray dest = Heap.getOopArray(args[2]);

            Type srcType = componentType(src);
            Type destType = componentType(dest);

            if (srcType.isPrimitive() && srcType != destType) {
                throw new ArrayStoreException("Type mismatch: " + srcType + " and " + destType);
            } else if (!srcType.isPrimitive()) {
                // TODO: move isInstanceOf around - might need to be a Type property?
            }

            int width = srcType.getWidth();

            int srcPos = args[1] * width;
            int destPos = args[3] * width;
            int len = args[4] * width;

            System.arraycopy(src.getFields(), srcPos, dest.getFields(), destPos, len);

            return NativeReturn.forVoid();
        }
    },
    CURRENT_TIME_MILLIS("currentTimeMillis", "()J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forLong(System.currentTimeMillis());
        }
    },
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {

            // Already done
            return NativeReturn.forVoid();
        }
    },
    NANO_TIME("nanoTime", "()J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forLong(System.nanoTime());
        }
    },
    IDENTITY_HASH_CODE("identityHashCode", "(Ljava/lang/Object;)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // TODO: this is NOT how this works!
            return ObjectNatives.HASHCODE.execute(args, ctx);
        }
    },
    SET_OUT_0("setOut0", "(Ljava/io/PrintStream;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            Field out = system.findField("out", true);
            out.set(null, args[0]);
            return NativeReturn.forVoid();
        }
    },
    SET_ERR_0("setErr0", "(Ljava/io/PrintStream;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            Field err = system.findField("err", true);
            err.set(null, args[0]);
            return NativeReturn.forVoid();
        }
    },
    SET_IN_0("setIn0", "(Ljava/io/InputStream;)V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            system.findField("in", true).set(null, args[0]);
            return NativeReturn.forVoid();
        }
    },
    INIT_PROPERTIES("initProperties", "(Ljava/util/Properties;)Ljava/util/Properties;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClass props = Heap.getOopClass(args[0]);
            Method setProperty = props.getClassObject().findMethod("setProperty", false);

            for (String key : System.getProperties().stringPropertyNames()) {
                int[] spArgs = setProperty.newArgArray();
                spArgs[0] = props.getAddress();
                spArgs[1] = Heap.intern(key);
                spArgs[2] = Heap.intern(System.getProperty(key));
                Utils.executeMethodAndThrow(setProperty, spArgs, ctx.getThread());
            }

            return NativeReturn.forReference(props);
        }
    },
    MAP_LIBRARY_NAME("mapLibraryName", "(Ljava/lang/String;)Ljava/lang/String;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(Heap.intern(System.mapLibraryName(Utils.toString(args[0]))));
        }
    };

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