package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.ClassCache;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.oop.OopClass;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum ClassNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forVoid();
        }
    },
    DESIRED_ASSERTION_STATUS_0("desiredAssertionStatus0", "(Ljava/lang/Class;)Z") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forInt(0); // false
        }
    },
    GET_CLASSLOADER_0("getClassLoader0", "()Ljava/lang/ClassLoader;") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forNull();
        }
    },
    GET_PRIMITIVE_CLASS("getPrimitiveClass", "(Ljava/lang/String;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args) {
            OopClass stringObj = Heap.getOopClass(args[0]);
            String arg = Utils.toString(stringObj);
            SimpleType st = SimpleType.valueOf(arg.toUpperCase());
            return NativeReturn.forInt(ClassCache.getOopPrimitive(st).getAddress());
        }
    };

    private final MethodSignature methodSignature;

    private ClassNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return MagicClasses.JAVA_LANG_CLASS;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
