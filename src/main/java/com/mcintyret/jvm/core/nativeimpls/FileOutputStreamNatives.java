package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.opcode.OperationContext;

public enum FileOutputStreamNatives implements NativeImplementation {
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Do nothing??
            return NativeReturn.forVoid();
        }
    };

    private final MethodSignature methodSignature;

    private FileOutputStreamNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/io/FileOutputStream";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}