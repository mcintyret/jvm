package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;

public enum AccessControllerNatives implements NativeImplementation {
    DO_PRIVILEGED("doPrivileged", "(Ljava/security/PrivilegedAction;)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // Meh, I'm sure it's fine...
            OopClass privilegedAction = args.getOop(0);
            Method run = privilegedAction.getClassObject().findMethod("run", "()Ljava/lang/Object;", false);
            Variables runArgs = run.newArgArray();
            runArgs.putOop(0, privilegedAction);
            return Utils.executeMethodAndThrow(run, runArgs, ctx.getThread());
        }

        @Override
        public String getClassName() {
            return "java/security/AccessController";
        }
    },
    GET_STACK_ACCESS_CONTROL_CONTEXT("getStackAccessControlContext", "()Ljava/security/AccessControlContext;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
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