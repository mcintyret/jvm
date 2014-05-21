package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.domain.MethodSignature;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum ClassNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(int[] args) {
            return NativeReturn.forVoid();
        }
    };

    private final MethodSignature methodSignature;

    private ClassNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return MagicClasses.JAVA_LANG_CLASS;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
