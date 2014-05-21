package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.domain.MethodSignature;
import org.reflections.Reflections;

import java.util.Set;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum ObjectNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            Reflections reflections = new Reflections("com.mcintyret.jvm.core.nativeimpls");
            Set<Class<? extends NativeImplementation>> classes = reflections.getSubTypesOf(NativeImplementation.class);

            for (Class<? extends NativeImplementation> clazz : classes) {
                if (clazz.isEnum()) {
                    for (NativeImplementation val : clazz.getEnumConstants()) {
                        if (val != this) {
                            NativeImplemntationRegistry.registerNative(val);
                        }
                    }
                }
            }
            return NativeReturn.forVoid();
        }
    },
    NOTIFY("notify", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            // TODO: implement wait/notify!
            return NativeReturn.forVoid();
        }
    },
    HASHCODE("hashCode", "()I") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forInt(Heap.getOop(args[0]).getAddress());
        }
    };


    private final MethodSignature methodSignature;

    private ObjectNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return MagicClasses.JAVA_LANG_OBJECT;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
