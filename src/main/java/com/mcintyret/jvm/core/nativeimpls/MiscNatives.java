package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.opcode.OperationContext;

public enum MiscNatives implements NativeImplementation {
    SUN_MISC_VM_INITIALIZE("initialize", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "sun/misc/VM";
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
