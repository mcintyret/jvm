package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.MethodSignature;

/**
 * User: tommcintyre
 * Date: 12/4/14
 */
public enum ArrayNatives implements NativeImplementation {
    NEW_ARRAY("newArray", "(Ljava/lang/Class;I)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopClassClass componentClass = args.getOop(0);
            ArrayType arrayType = ArrayType.create(componentClass.getThisType(), 1);

            return NativeReturn.forReference(arrayType.getClassObject().newArray(args.getInt(1)));
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