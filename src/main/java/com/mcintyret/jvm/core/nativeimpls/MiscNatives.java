package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopClassMethod;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.load.*;

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
    STRING_INTERN("intern", "()Ljava/lang/String;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(Heap.intern(Utils.toString(Heap.getOopClass(args[0]))));
        }

        @Override
        public String getClassName() {
            return "java/lang/String";
        }
    },
    FILE_DESCRIPTOR_SET_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // WTF do i do here??
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "java/io/FileDescriptor";
        }
    },
    CONSTRUCTOR_NEW_INSTANCE("newInstance0", "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Method ctor;
            OopClass oop = Heap.getOopClass(args[0]);
            if (oop instanceof OopClassMethod) {
                ctor = ((OopClassMethod) oop).getMethod();
            } else {
                // This happens when the Constructor method was created by using non-native methods
                // TODO: could this be nicer??
                ClassObject ctorClass = oop.getClassObject();
                Field declaringClassField = ctorClass.findField("clazz", false);
                ClassObject declaringClass = (ClassObject) ((OopClassClass) declaringClassField.getOop(oop)).getThisClass();

                Field signatureField = ctorClass.findField("signature", false);
                String sig = Utils.toString(signatureField.getInt(oop));

                ctor = declaringClass.findConstructor(sig.substring(sig.indexOf(':') + 1));

            }

            ClassObject co = ctor.getClassObject();
            OopClass instance = co.newObject();

            int[] ctorArgs = ctor.newArgArray();
            ctorArgs[0] = Heap.allocate(instance);

            OopArray givenArgs = Heap.getOopArray(args[1]);
            int[] argInts = givenArgs == null ? new int[0] : givenArgs.getFields();
            System.arraycopy(argInts, 0, ctorArgs, 1, argInts.length);

            Utils.executeMethodAndThrow(ctor, ctorArgs, ctx.getExecutionStack().getThread());

            return NativeReturn.forReference(instance);
        }

        @Override
        public String getClassName() {
            return "sun/reflect/NativeConstructorAccessorImpl";
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
