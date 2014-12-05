package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.MethodSignature;

/**
 * User: tommcintyre
 * Date: 7/16/14
 */
public enum SignalNatives implements NativeImplementation {
    FIND_SIGNAL("findSignal", "(Ljava/lang/String;)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(0); // TODO
        }
    },
    HANDLE_0("handle0", "(IJ)J") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forLong(0L);
        }
    };

    private final MethodSignature methodSignature;

    private SignalNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "sun/misc/Signal";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
