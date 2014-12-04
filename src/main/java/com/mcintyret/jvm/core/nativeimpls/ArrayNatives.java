package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.opcode.OperationContext;

/**
 * User: tommcintyre
 * Date: 12/4/14
 */
public enum ArrayNatives implements NativeImplementation {
    NEW_ARRAY("newArray", "(Ljava/lang/Class;I)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type componentType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            ArrayType arrayType = ArrayType.create(componentType, 1);

            return NativeReturn.forReference(arrayType.getClassObject().newArray(args[1]));
        }
    };

    private final MethodSignature methodSignature;

    private ArrayNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/lang/reflect/Array";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}