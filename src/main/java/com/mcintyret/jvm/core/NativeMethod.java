package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeExecution;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class NativeMethod extends Method {

    private final NativeExecution nativeExecution;

    public NativeMethod(MethodSignature methodSignature, Set<Modifier> modifiers, NativeExecution nativeExecution) {
        super(null, methodSignature, modifiers, -1);
        this.nativeExecution = nativeExecution;
    }

    public NativeExecution getNativeExecution() {
        return nativeExecution;
    }
}
