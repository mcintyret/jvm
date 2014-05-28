package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.opcode.OperationContext;

public enum MiscNatives implements NativeImplementation {
    SUN_MISC_VM_INITIALIZE("initialize", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "sun/misc/VM";
        }
    },
    SECURITY_ACCESSCONTROLLER_DOPRIVILEGED("doPrivileged", "(Ljava/security/PrivilegedAction;)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // Meh, I'm sure it's fine...
            OopClass privilegedAction = Heap.getOopClass(args[0]);
            Method run = privilegedAction.getClassObject().findMethod("run", "()Ljava/lang/Object;", false);
            return Utils.executeMethod(run, new int[run.getCode().getMaxLocals()], ctx.getExecutionStack().getThread());
        }

        @Override
        public String getClassName() {
            return "java/security/AccessController";
        }
    };

    private final MethodSignature methodSignature;

    private MiscNatives(String name, String desc) {
        this.methodSignature = MethodSignature.parse(name, desc);
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
