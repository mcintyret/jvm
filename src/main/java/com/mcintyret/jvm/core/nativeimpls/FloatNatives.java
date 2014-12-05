package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.MethodSignature;

public enum FloatNatives implements NativeImplementation {
    FLOAT_TO_RAW_INT_BITS("floatToRawIntBits", "(F)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // Already in that state
            return NativeReturn.forInt(args.getRawValue(0));
        }
    };

    private final MethodSignature methodSignature;

    private FloatNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/Float";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}
