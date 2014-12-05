package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.MethodSignature;

public enum DoubleNatives implements NativeImplementation {
    DOUBLE_TO_RAW_LONG_BITS("doubleToRawLongBits", "(D)J") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // Already in that state
            return NativeReturn.forLong(args.getLong(0));
        }
    };

    private final MethodSignature methodSignature;

    private DoubleNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/lang/Double";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
