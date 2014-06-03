package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OperationContext;

public enum AccessControllerNatives implements NativeImplementation {
    DO_PRIVILEGED("doPrivileged", "(Ljava/security/PrivilegedAction;)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Meh, I'm sure it's fine...
            OopClass privilegedAction = Heap.getOopClass(args[0]);
            Method run = privilegedAction.getClassObject().findMethod("run", "()Ljava/lang/Object;", false);
            int[] runArgs = run.newArgArray();
            runArgs[0] = args[0];
            return Utils.executeMethod(run, runArgs, ctx.getExecutionStack().getThread());
        }

        @Override
        public String getClassName() {
            return "java/security/AccessController";
        }
    },
    GET_STACK_ACCESS_CONTROL_CONTEXT("getStackAccessControlContext", "()Ljava/security/AccessControlContext;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forNull();
        }
    };

    private final MethodSignature methodSignature;

    private AccessControllerNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/security/AccessController";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}