package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

public enum UnsafeNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            // do nothing for now
            return NativeReturn.forVoid();
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