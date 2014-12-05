package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;
import sun.misc.SharedSecrets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum FileOutputStreamNatives implements NativeImplementation {
    CLOSE_0("close0", "()V") {
        @Override
        public NativeReturn execute(Variable[] args, OperationContext ctx) {
            int fd = getFileDescriptor(args[0].getOop());
            FileOutputStream fos = OPEN_FOSES.remove(fd);
            try {
                fos.close();
                return NativeReturn.forVoid();
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variable[] args, OperationContext ctx) {
            // Do nothing??
            return NativeReturn.forVoid();
        }
    },
    OPEN("open", "(Ljava/lang/String;Z)V") {
        @Override
        public NativeReturn execute(Variable[] args, OperationContext ctx) {
            String name = Utils.toString(args[1].getOop());
            boolean append = args[2].getRawValue() > 0;

            try {
                FileOutputStream fos = new FileOutputStream(new File(name), append);
                int fd = SharedSecrets.getJavaIOFileDescriptorAccess().get(fos.getFD());

                OPEN_FOSES.put(fd, fos);
                return NativeReturn.forVoid();
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    },
    WRITE_BYTES("writeBytes", "([BIIZ)V") {
        @Override
        public NativeReturn execute(Variable[] args, OperationContext ctx) {
            FileOutputStream fos = OPEN_FOSES.get(getFileDescriptor(args[0].getOop()));
            OopArray bytesOop = args[1].getOop();
            byte[] bytes = new byte[bytesOop.getLength()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) bytesOop.getFields()[i];
            }

            try {
                fos.write(bytes, args[2].getRawValue(), args[3].getRawValue());
                return NativeReturn.forVoid();
            } catch (IOException e) {
                return NativeReturn.forThrowable(Utils.toThrowableOop(e, ctx.getThread()));
            }
        }
    };

    private static final Map<Integer, FileOutputStream> OPEN_FOSES = new HashMap<>();

    static {
        OPEN_FOSES.put(1, getFos(System.out));
        OPEN_FOSES.put(2, getFos(System.err));
    }

    private static FileOutputStream getFos(FilterOutputStream filterStream) {
        try {
            Field outField = FilterOutputStream.class.getDeclaredField("out");
            outField.setAccessible(true);

            Object out = outField.get(filterStream);
            if (out instanceof FileOutputStream) {
                return (FileOutputStream) out;
            } else {
                return getFos((FilterOutputStream) out);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int getFileDescriptor(OopClass fosOop) {
        OopClass fdOop = (OopClass) fosOop.getClassObject().findField("fd", false).getOop(fosOop);
        return fdOop.getClassObject().findField("fd", false).getInt(fdOop);
    }

    private final MethodSignature methodSignature;

    private FileOutputStreamNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/io/FileOutputStream";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}