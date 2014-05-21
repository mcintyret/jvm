package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class NativeImplemntationRegistry {

    private static final Map<ClassNameAndMethodSignature, NativeImplementation> NATIVE_EXECUTION_MAP = new HashMap<>();

    public static void registerNative(NativeImplementation nativeImplementation) {
        ClassNameAndMethodSignature key = new ClassNameAndMethodSignature(nativeImplementation);
        System.out.println("Loading native implementation for " + key);
        if (NATIVE_EXECUTION_MAP.put(key, nativeImplementation) != null) {
            throw new IllegalArgumentException("NativeImplementation already exists for " + key);
        }
    }

    public static NativeImplementation getNativeExecution(String className, MethodSignature methodSignature) {
        return NATIVE_EXECUTION_MAP.get(new ClassNameAndMethodSignature(className, methodSignature));
    }

}
