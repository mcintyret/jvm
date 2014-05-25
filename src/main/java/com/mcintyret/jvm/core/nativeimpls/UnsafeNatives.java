package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

public enum UnsafeNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            // do nothing for now
            return NativeReturn.forVoid();
        }
    },
    ARRAY_BASE_OFFSET("arrayBaseOffset", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forInt(args[0]);
        }
    },
    ARRAY_INDEX_SCALE("arrayIndexScale", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args) {
            // TODO: do this properly
            return NativeReturn.forInt(4);
        }
    },
    ADDRESS_SIZE("addressSize", "()I") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forInt(4);
        }
    };


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