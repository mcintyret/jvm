package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;
import sun.misc.SharedSecrets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum FileInputStreamNatives implements NativeImplementation {
    CLOSE_0("close0", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            int fd = getFileDescriptor(args.getOop(0));
            FileInputStream fis = OPEN_FISES.remove(fd);
            try {
                fis.close();
                return NativeReturn.forVoid();
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    AVAILABLE("available", "()I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            try {
                return NativeReturn.forInt(OPEN_FISES.get(getFileDescriptor(args.getOop(0))).available());
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    OPEN("open", "(Ljava/lang/String;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            String name = Utils.toString(args.<OopClass>getOop(1));

            try {
                FileInputStream fis = new FileInputStream(new File(name));
                int fd = SharedSecrets.getJavaIOFileDescriptorAccess().get(fis.getFD());
                setFileDescriptor(args.getOop(0), fd);

                OPEN_FISES.put(fd, fis);
                return NativeReturn.forVoid();
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    READ_BYTES("readBytes", "([BII)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            FileInputStream fis = OPEN_FISES.get(getFileDescriptor(args.getOop(0)));
            OopArray bytesOop = args.getOop(1);
            byte[] bytes = new byte[bytesOop.getLength()];

            try {
                int ret = fis.read(bytes, args.getInt(2), args.getInt(3));

                for (int i = 0; i < bytes.length; i++) {
                    bytesOop.getFields().getRawValues()[i] = bytes[i];
                }

                return NativeReturn.forInt(ret);
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // Do nothing??
            return NativeReturn.forVoid();
        }
    };

    private static final Map<Integer, FileInputStream> OPEN_FISES = new HashMap<>();

    static {
        OPEN_FISES.put(0, getFis((FilterInputStream) System.in));
    }

    private static FileInputStream getFis(FilterInputStream filterStream) {
        try {
            Field inField = FilterInputStream.class.getDeclaredField("in");
            inField.setAccessible(true);

            Object in = inField.get(filterStream);
            if (in instanceof FileInputStream) {
                return (FileInputStream) in;
            } else {
                return getFis((FilterInputStream) in);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int getFileDescriptor(OopClass fisOop) {
        OopClass fdOop = (OopClass) fisOop.getClassObject().findField("fd", false).getOop(fisOop);
        return fdOop.getClassObject().findField("fd", false).getInt(fdOop);
    }

    private static void setFileDescriptor(OopClass fisOop, int fd) {
        OopClass fdOop = (OopClass) fisOop.getClassObject().findField("fd", false).getOop(fisOop);
        fdOop.getClassObject().findField("fd", false).set(fdOop, fd);
    }

    private final MethodSignature methodSignature;

    private FileInputStreamNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/io/FileInputStream";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}