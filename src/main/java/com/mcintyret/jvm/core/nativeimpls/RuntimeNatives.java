package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.opcode.OperationContext;

public enum RuntimeNatives implements NativeImplementation {
    FREE_MEMORY("freeMemory", "()J") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forLong(Runtime.getRuntime().freeMemory());
        }
    },
    AVAILABLE_PROCESSORS("availableProcessors", "()I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(Runtime.getRuntime().availableProcessors());
        }
    };

    private final MethodSignature methodSignature;

    private RuntimeNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/Runtime";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}