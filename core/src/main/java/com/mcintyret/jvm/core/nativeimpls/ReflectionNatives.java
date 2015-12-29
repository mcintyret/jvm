package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.Execution;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.ReferenceType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.parse.Modifier;

import java.util.Iterator;

/**
 * User: tommcintyre
 * Date: 5/25/14
 */
public enum ReflectionNatives implements NativeImplementation {
    GET_CALLER_CLASS("getCallerClass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // Literally no documentation in the world about what this no-arg form is.
            // TODO: although I could make an educated guess...
            Execution callerExecution = getSecond(ctx.getExecutionStack().getStack().iterator());
            return NativeReturn.forReference(callerExecution.getMethod().getClassObject().getOop());
        }
    },
    GET_CLASS_ACCESS_FLAGS("getClassAccessFlags", "(Ljava/lang/Class;)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopClassClass thisClass = args.getOop(0);
            Type type = thisClass.getThisType();
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

    private static <T> T getSecond(Iterator<T> it) {
        it.next();
        return it.next();
    }
}