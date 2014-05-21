package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class NativeExecutionRegistry {

    private static final Map<ClassNameAndMethodSignature, NativeExecution> NATIVE_EXECUTION_MAP = new HashMap<>();

    public static void registerNativeExecution(NativeExecution nativeExecution) {
        ClassNameAndMethodSignature key = new ClassNameAndMethodSignature(nativeExecution);
        if (NATIVE_EXECUTION_MAP.put(key, nativeExecution) != null) {
            throw new IllegalArgumentException("NativeExecution already exists for " + key);
        }
    }

    public static NativeExecution getNativeExecution(String className, MethodSignature methodSignature) {
        return NATIVE_EXECUTION_MAP.get(new ClassNameAndMethodSignature(className, methodSignature));
    }

}
