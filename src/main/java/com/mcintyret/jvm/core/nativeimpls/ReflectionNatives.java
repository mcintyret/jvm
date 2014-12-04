package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.NonArrayType;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.parse.Modifier;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
public enum ReflectionNatives implements NativeImplementation {
    GET_CALLER_CLASS("getCallerClass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Literally no documentation in the world about what this no-arg form is.
            // TODO: although I could make an educated guess...
            return NativeReturn.forReference(NonArrayType.forClass("java/lang/Object").getOopClassClass());
        }
    },
    GET_CLASS_ACCESS_FLAGS("getClassAccessFlags", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type type = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            if (type.isPrimitive()) {
                throw new IllegalStateException("Don't know what to do here!");
            } else {
                return NativeReturn.forInt(Modifier.translate(((ReferenceType) type).getClassObject().getModifiers()));
            }
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