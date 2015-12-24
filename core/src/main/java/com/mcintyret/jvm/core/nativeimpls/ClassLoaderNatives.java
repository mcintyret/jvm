package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.exec.OperationContext;

public enum ClassLoaderNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    };

    private final MethodSignature methodSignature;

    private ClassLoaderNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/ClassLoader";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}

