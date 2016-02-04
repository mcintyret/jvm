package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;

import static com.mcintyret.jvm.load.ClassLoader.getClassLoader;

public enum ClassLoaderNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    FIND_LOADED_CLASS0("findLoadedClass0", "(Ljava/lang/String;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            String className = Utils.toJvmClassName(Utils.toString((OopClass) args.getOop(1)));

            ClassObject co = getClassLoader().getClassObject(className);
            return NativeReturn.forReference(co.getOop());
        }
    };

    private final MethodSignature methodSignature;

    private ClassLoaderNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/ClassLoader";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}

