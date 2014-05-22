package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.domain.MethodSignature;

public enum DoubleNatives implements NativeImplementation {
    DOUBLE_TO_RAW_LONG_BITS("doubleToRawLongBits", "(D)J") {
        @Override
        public NativeReturn execute(int[] args) {
            // Already in that state
            return NativeReturn.forLong(Utils.toLong(args[0], args[1]));
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
