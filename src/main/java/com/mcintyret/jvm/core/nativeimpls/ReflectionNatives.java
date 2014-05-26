package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.clazz.ClassCache;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.NonArrayType;
import com.mcintyret.jvm.core.opcode.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
public enum ReflectionNatives implements NativeImplementation {
    GET_CALLER_CLASS("getCallerClass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Literally no documentation in the world about what this no-arg form is.
            return NativeReturn.forInt(ClassCache.getOopClass(NonArrayType.forClass("java/lang/Object")).getAddress());
        }
    };


    private final MethodSignature methodSignature;

    private ReflectionNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "sun/reflect/Reflection";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}