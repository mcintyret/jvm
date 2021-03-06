package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopClassMethod;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.util.ReflectionUtils;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassLoader;

import java.util.concurrent.atomic.AtomicLong;

public enum MiscNatives implements NativeImplementation {
    ATOMIC_LONG_VM_SUPPORTS_CS8("VMSupportsCS8", "()Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = ReflectionUtils.executeStaticMethod(AtomicLong.class, "VMSupportsCS8");
            return NativeReturn.forBool((Boolean) ret);
        }

        @Override
        public String getClassName() {
            return "java/util/concurrent/atomic/AtomicLong";
        }
    },
    SUN_MISC_VM_INITIALIZE("initialize", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "sun/misc/VM";
        }
    },
    SUN_MISC_PERF_REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "sun/misc/Perf";
        }
    },
    SUN_MISC_PERF_CREATE_LONG("createLong", "(Ljava/lang/String;IIJ)Ljava/nio/ByteBuffer;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // This is getting pretty deep into stuff I don't want to worry about...
            // For a decent explanation of what this is about see here: http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b27/sun/misc/Perf.java#Perf.createLong%28java.lang.String%2Cint%2Cint%2Clong%29
            ClassObject byteBufferClass = ClassLoader.getClassLoader().getClassObject("java/nio/ByteBuffer");
            Method allocate = byteBufferClass.findMethod("allocate", true);

            Variables allocateArgs = allocate.newArgArray();
            allocateArgs.putInt(0, 10);

            return Utils.executeMethodAndThrow(allocate, allocateArgs, ctx.getThread());
        }

        @Override
        public String getClassName() {
            return "sun/misc/Perf";
        }
    },
    JAR_FILE_GET_META_INF_ENTRY_NAMES("getMetaInfEntryNames", "()[Ljava/lang/String;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forNull();
        }

        @Override
        public String getClassName() {
            return "java/util/jar/JarFile";
        }
    },
    STRING_INTERN("intern", "()Ljava/lang/String;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(Heap.intern(Utils.toString((OopClass) args.getOop(0))));
        }

        @Override
        public String getClassName() {
            return "java/lang/String";
        }
    },
    FILE_DESCRIPTOR_SET_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // WTF do i do here??
            return NativeReturn.forVoid();
        }

        @Override
        public String getClassName() {
            return "java/io/FileDescriptor";
        }
    },
    FILE_SYSTEM_GET_FILE_SYSTEM("getFileSystem", "()Ljava/io/FileSystem;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            ClassObject unixFileSystem = ClassLoader.getClassLoader().getClassObject("java/io/UnixFileSystem");

            return NativeReturn.forReference(Utils.construct(unixFileSystem, ctx.getThread()));
        }

        @Override
        public String getClassName() {
            return "java/io/FileSystem";
        }
    },
    CONSTRUCTOR_NEW_INSTANCE("newInstance0", "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Method ctor;
            OopClass oop = args.getOop(0);
            if (oop instanceof OopClassMethod) {
                ctor = ((OopClassMethod) oop).getMethod();
            } else {
                // This happens when the Constructor method was created by using non-native methods
                // TODO: could this be nicer??
                ClassObject ctorClass = oop.getClassObject();
                Field declaringClassField = ctorClass.findField("clazz", false);
                OopClassClass declaringClassClass = (OopClassClass) declaringClassField.getOop(oop);
                ClassObject declaringClass = ((NonArrayType) declaringClassClass.getThisType()).getClassObject();

                Field signatureField = ctorClass.findField("signature", false);
                String sig = Utils.toString(signatureField.getInt(oop));

                ctor = declaringClass.findConstructor(sig.substring(sig.indexOf(':') + 1));

            }

            ClassObject co = ctor.getClassObject();
            OopClass instance = co.newObject();
            Heap.allocate(instance);

            Variables ctorArgs = ctor.newArgArray();
            ctorArgs.putOop(0, instance);

            OopArray givenArgs = args.getOop(1);
            Variables argInts = givenArgs == null ? Variables.EMPTY_VARIABLES : givenArgs.getFields();
            Variables.copyInto(argInts, ctorArgs);

            Utils.executeMethodAndThrow(ctor, ctorArgs, ctx.getThread());

            return NativeReturn.forReference(instance);
        }

        @Override
        public String getClassName() {
            return "sun/reflect/NativeConstructorAccessorImpl";
        }
    },
    NATIVE_LIBRARY_LOAD("load", "(Ljava/lang/String;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // TODO - might use reflection to do the real thing?
            OopClass nativeLibrary = args.getOop(0);
            ClassObject clazz = nativeLibrary.getClassObject();
            clazz.findField("handle", false).set(nativeLibrary, 1L);
            return NativeReturn.forVoid(); // Hope this works!
        }

        @Override
        public String getClassName() {
            return "java/lang/ClassLoader$NativeLibrary";
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
