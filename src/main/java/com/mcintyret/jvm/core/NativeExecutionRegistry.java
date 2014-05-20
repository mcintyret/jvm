package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.MethodSignature;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class NativeExecutionRegistry {

    private static final Map<ClassNameAndMethodSignature, NativeExecution> NATIVE_EXECUTION_MAP = new HashMap<>();

    public static void registerNativeExecution(String className, MethodSignature methodSignature, NativeExecution nativeExecution) {
        if (NATIVE_EXECUTION_MAP.put(new ClassNameAndMethodSignature(className, methodSignature), nativeExecution) != null) {
            throw new IllegalArgumentException("NativeExecution already exists for " + className + "." + methodSignature);
        }
    }

    public static NativeExecution getNativeExecution(String className, MethodSignature methodSignature) {
        return NATIVE_EXECUTION_MAP.get(new ClassNameAndMethodSignature(className, methodSignature));
    }

    static class ClassNameAndMethodSignature {

        private final String className;

        private final MethodSignature methodSignature;

        private ClassNameAndMethodSignature(String className, MethodSignature methodSignature) {
            this.className = className;
            this.methodSignature = methodSignature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassNameAndMethodSignature that = (ClassNameAndMethodSignature) o;

            if (!className.equals(that.className)) return false;
            if (!methodSignature.equals(that.methodSignature)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + methodSignature.hashCode();
            return result;
        }
    }

}
