package com.mcintyret.jvm.test;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;

// TODO: take out of core module
public enum TestNativeImpls implements NativeImplementation {
    PRINT("print", "(Ljava/lang/String;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            System.out.println("NATIVE PRINT: " + Utils.toString(args.<OopClass>getOop(0)));
            return NativeReturn.forVoid();
        }
    },


    INCREMENT_NATIVE_INSTANCE("incrementNativeInstance", "([I)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray array = args.getOop(1);
            array.getFields().getRawValues()[0]++;
            return NativeReturn.forVoid();
        }
    },

    INCREMENT_NATIVE_STATIC("incrementNativeStatic", "([I)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray array = args.getOop(0);
            array.getFields().getRawValues()[0]++;
            return NativeReturn.forVoid();
        }
    },

    INCREMENT_NATIVE_INSTANCE_EXCEPTION("incrementNativeInstanceException", "([I)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray array = args.getOop(1);
            if (array.getFields().getRawValues()[0]++ % 2 == 0) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(new RuntimeException(), ctx.getThread()));
            }
            array.getFields().getRawValues()[1]++;
            return NativeReturn.forVoid();
        }
    },

    INCREMENT_NATIVE_STATIC_EXCEPTION("incrementNativeStaticException", "([I)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray array = args.getOop(0);
            if (array.getFields().getRawValues()[0]++ % 2 == 0) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(new RuntimeException(), ctx.getThread()));
            }
            array.getFields().getRawValues()[1]++;
            return NativeReturn.forVoid();
        }
    };

    private final MethodSignature methodSignature;

    private TestNativeImpls(String name, String signature) {
        this.methodSignature = MethodSignature.parse(name, signature);
    }

    @Override
    public String getClassName() {
        return "/com/mcintyret2/jvm/test/jvm/Main";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
