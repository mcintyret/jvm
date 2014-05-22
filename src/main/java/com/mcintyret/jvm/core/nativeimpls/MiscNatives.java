package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.domain.MethodSignature;
import java.util.Arrays;

public enum MiscNatives implements NativeImplementation {
    SUN_MISC_VM_INITIALIZE("initialize", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "sun/misc/VM";
        }
    },
    JAVA_UTIL_DICTIONARY_HASHCODE("hashCode", "()I") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forInt(Arrays.hashCode(Heap.getOop(args[0]).getFields()));
        }

        @Override
        public String getClassName() {
            return "java/util/Dictionary";
        }
    };

    private final MethodSignature methodSignature;

    private MiscNatives(String name, String desc) {
        this.methodSignature = MethodSignature.parse(name, desc);
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
