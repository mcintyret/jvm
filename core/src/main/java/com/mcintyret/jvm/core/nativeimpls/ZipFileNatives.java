package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.ReflectionUtils;
import com.mcintyret.jvm.core.util.Utils;

import java.lang.reflect.Method;
import java.util.zip.ZipFile;

import static com.mcintyret.jvm.core.util.ReflectionUtils.executeMethod;

public enum ZipFileNatives implements NativeImplementation {
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    OPEN("open", "(Ljava/lang/String;IJZ)J") {
        private final Method ZIP_FILE_OPEN = findZipFileMethod("open");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            String name = Utils.toString((OopClass) args.getOop(0));
            Object ret = executeMethod(ZIP_FILE_OPEN,
                name,
                args.getInt(1),
                args.getLong(2),
                args.getBoolean(4) // because the previous arg was double wide
            );
            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_TOTAL("getTotal", "(J)I") {

        private final Method ZIP_FILE_GET_TOTAL = findZipFileMethod("getTotal");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_TOTAL, args.getLong(0));

            return NativeReturn.forInt((Integer) ret);
        }
    },
    GET_ENTRY("getEntry", "(J[BZ)J") {

        private final Method ZIP_FILE_GET_ENTRY = findZipFileMethod("getEntry");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {

            OopArray byteArrayOop = args.getOop(2);
            byte[] byteArray = Utils.convertByteArrayOop(byteArrayOop);

            Object ret = executeMethod(ZIP_FILE_GET_ENTRY,
                args.getLong(0),
                byteArray,
                args.getBoolean(3)
            );

            for (int i = 0; i < byteArray.length; i++) {
                byteArrayOop.getFields().put(i, SimpleType.BYTE, byteArray[i]);
            }

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_FLAG("getEntryFlag", "(J)I") {

        private final Method ZIP_FILE_GET_ENTRY_FLAG = findZipFileMethod("getEntryFlag");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_FLAG, args.getLong(0));

            return NativeReturn.forInt((Integer) ret);
        }
    },
    GET_NEXT_ENTRY("getNextEntry", "(JI)J") {

        private final Method ZIP_FILE_GET_NEXT_ENTRY = findZipFileMethod("getNextEntry");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_NEXT_ENTRY, args.getLong(0), args.getInt(2));

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_METHOD("getEntryMethod", "(J)I") {

        private final Method ZIP_FILE_GET_ENTRY_METHOD = findZipFileMethod("getEntryMethod");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_METHOD, args.getLong(0));

            return NativeReturn.forInt((Integer) ret);
        }
    },
    GET_ENTRY_TIME("getEntryTime", "(J)J") {

        private final Method ZIP_FILE_GET_ENTRY_TIME = findZipFileMethod("getEntryTime");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_TIME, args.getLong(0));

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_CRC("getEntryCrc", "(J)J") {

        private final Method ZIP_FILE_GET_ENTRY_CRC = findZipFileMethod("getEntryCrc");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_CRC, args.getLong(0));

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_SIZE("getEntrySize", "(J)J") {

        private final Method ZIP_FILE_GET_ENTRY_SIZE = findZipFileMethod("getEntrySize");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_SIZE, args.getLong(0));

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_C_SIZE("getEntryCSize", "(J)J") {

        private final Method ZIP_FILE_GET_ENTRY_C_SIZE = findZipFileMethod("getEntryCSize");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_C_SIZE, args.getLong(0));

            return NativeReturn.forLong((Long) ret);
        }
    },
    GET_ENTRY_BYTES("getEntryBytes", "(JI)[B") {

        private final Method ZIP_FILE_GET_ENTRY_BYTES = findZipFileMethod("getEntryBytes");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_GET_ENTRY_BYTES, args.getLong(0), args.getInt(2));

            if (ret == null) {
                return NativeReturn.forNull();
            }

            return NativeReturn.forReference(Utils.convertByteArray((byte[]) ret));
        }
    },
    FREE_ENTRY("freeEntry", "(JJ)V") {

        private final Method ZIP_FILE_FREE_ENTRY = findZipFileMethod("freeEntry");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            executeMethod(ZIP_FILE_FREE_ENTRY, args.getLong(0), args.getLong(2));

            return NativeReturn.forVoid();
        }
    },
    READ("read", "(JJJ[BII)I") {

        private final Method ZIP_FILE_READ = findZipFileMethod("read");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray oopBytes = args.getOop(6);
            byte[] byteArray = Utils.convertByteArrayOop(oopBytes);

            Object ret = executeMethod(ZIP_FILE_READ,
                args.getLong(0),
                args.getLong(2),
                args.getLong(4),
                byteArray,
                args.getInt(7),
                args.getInt(8));

            // Actually update the byte array we're reading into
            for (int i = 0; i < byteArray.length; i++) {
                oopBytes.getFields().put(i, SimpleType.BYTE, byteArray[i]);
            }

            return NativeReturn.forInt((Integer) ret);
        }
    },
    STARTS_WITH_LOC("startsWithLOC", "(J)Z") {

        private final Method ZIP_FILE_STARTS_WITH_LOC = findZipFileMethod("startsWithLOC");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(ZIP_FILE_STARTS_WITH_LOC, args.getLong(0));

            return NativeReturn.forBool((Boolean) ret);
        }
    };

    private static Method findZipFileMethod(String name) {
        return ReflectionUtils.findMethod(ZipFile.class, name, true);
    }

    private final MethodSignature methodSignature;

    private ZipFileNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/util/zip/ZipFile";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}
