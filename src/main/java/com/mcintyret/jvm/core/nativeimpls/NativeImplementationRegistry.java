package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.type.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class NativeImplementationRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(NativeImplementationRegistry.class);

    private static final Map<ClassNameAndMethodSignature, NativeImplementation> NATIVE_EXECUTION_MAP = new HashMap<>();

    public static void registerNative(NativeImplementation nativeImplementation) {
        ClassNameAndMethodSignature key = new ClassNameAndMethodSignature(nativeImplementation);
        LOG.info("Loading native implementation for {}", key);
        if (NATIVE_EXECUTION_MAP.put(key, nativeImplementation) != null) {
            throw new IllegalArgumentException("NativeImplementation already exists for " + key);
        }
    }

    public static NativeImplementation getNativeExecution(String className, MethodSignature methodSignature) {
        return NATIVE_EXECUTION_MAP.get(new ClassNameAndMethodSignature(className, methodSignature));
    }

}
