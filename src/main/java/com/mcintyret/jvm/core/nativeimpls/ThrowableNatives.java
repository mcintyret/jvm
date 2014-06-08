package com.mcintyret.jvm.core.nativeimpls;

import java.util.Deque;

import com.google.common.collect.Iterables;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.load.ClassLoader;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public enum ThrowableNatives implements NativeImplementation {
    FILL_IN_STACK_TRACE("fillInStackTrace", "(I)Ljava/lang/Throwable;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Deque<ExecutionStackElement> stack = ctx.getExecutionStack().getStack();

            ClassObject stackTraceElemCo = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/StackTraceElement");
            OopArray stes = ArrayClassObject.forType(ArrayType.create(stackTraceElemCo.getType(), 1)).newArray(stack.size());

            Heap.getOop(args[0]).getFields()[3] = Heap.allocate(stes);

            int i = 0;
            Method ctor = stackTraceElemCo.findConstructor("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");
            for (ExecutionStackElement ese : stack) {
                OopClass ste = makeStackTraceElement(stackTraceElemCo, ctor, ese, ctx.getExecutionStack().getThread());

                stes.getFields()[i++] = ste.getAddress();
            }

            // Return this
            return NativeReturn.forInt(args[0]);
        }
    },
    GET_STACK_TRACE_DEPTH("getStackTraceDepth", "()I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(ctx.getExecutionStack().getStack().size());
        }
    },
    GET_STACK_TRACE_ELEMENT("getStackTraceElement", "(I)Ljava/lang/StackTraceElement;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            ClassObject stackTraceElemCo = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/StackTraceElement");
            Method ctor = stackTraceElemCo.findConstructor("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");
            ExecutionStackElement elem = Iterables.get(ctx.getExecutionStack().getStack(), args[1]);

            OopClass ste = makeStackTraceElement(stackTraceElemCo, ctor, elem, ctx.getExecutionStack().getThread());

            return NativeReturn.forInt(ste.getAddress());
        }
    };

    private static OopClass makeStackTraceElement(ClassObject stackTraceElemCo, Method ctor, ExecutionStackElement ese, Thread thread) {
        int[] ctorArgs = ctor.newArgArray();
        OopClass ste = stackTraceElemCo.newObject();

        Method m = ese.getMethod();
        ctorArgs[0] = Heap.allocate(ste);
        ctorArgs[1] = Heap.intern(m.getClassObject().getClassName());
        ctorArgs[2] = Heap.intern(m.getSignature().getName());
        ctorArgs[3] = Heap.NULL_POINTER;
        ctorArgs[4] = -1; // TODO: line number

        Utils.executeMethodAndThrow(ctor, ctorArgs, thread);
        return ste;
    }

    private final MethodSignature methodSignature;

    private ThrowableNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/Throwable";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}

