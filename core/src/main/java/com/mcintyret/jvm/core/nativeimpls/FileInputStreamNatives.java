package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.exec.OperationContext;

public enum FileInputStreamNatives implements NativeImplementation {
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Do nothing??
            return NativeReturn.forVoid();
        }
    };

    private final MethodSignature methodSignature;

    private FileInputStreamNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/io/FileInputStream";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}