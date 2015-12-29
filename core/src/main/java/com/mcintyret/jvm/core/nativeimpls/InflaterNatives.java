package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClassInflater;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

import java.lang.reflect.Method;
import java.util.zip.Inflater;

import static com.mcintyret.jvm.core.util.ReflectionUtils.executeMethod;
import static com.mcintyret.jvm.core.util.ReflectionUtils.findMethod;
import static com.mcintyret.jvm.core.util.ReflectionUtils.getFieldValue;
import static com.mcintyret.jvm.core.util.ReflectionUtils.setFieldValue;

public enum InflaterNatives implements NativeImplementation {

    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    INIT("init", "(Z)J") {

        private final Method INFLATER_INIT = findInflaterMethod("init");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Object ret = executeMethod(INFLATER_INIT, args.getBoolean(0));

            return NativeReturn.forLong((Long) ret);
        }
    },
    RESET("reset", "(J)V") {

        private final Method INFLATER_RESET = findInflaterMethod("reset");

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            executeMethod(INFLATER_RESET, args.getLong(0));

            return NativeReturn.forVoid();
        }
    },
    INFLATE_BYTES("inflateBytes", "(J[BII)I") {

        private final Method INFLATE_BYTES = findMethod(Inflater.class, "inflateBytes", false);

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray oopBytes = args.getOop(3);
            byte[] byteArray = new byte[oopBytes.getLength()];

            OopClassInflater inflaterOop = args.getOop(0);
            Inflater inflater = inflaterOop.getInflater();

            // get some state from the JVM inflater
            Variables inflaterFields = inflaterOop.getFields();
            byte[] toInflate = Utils.convertByteArrayOop(inflaterFields.getOop(1));
            setFieldValue(inflater, "buf", toInflate);
            setFieldValue(inflater, "off", inflaterFields.getInt(2));
            setFieldValue(inflater, "len", inflaterFields.getInt(3));
            setFieldValue(inflater, "finished", inflaterFields.getBoolean(4));
            setFieldValue(inflater, "needDict", inflaterFields.getBoolean(5));

            Object ret;
            try {
                ret = INFLATE_BYTES.invoke(inflater, args.getLong(1), byteArray, args.getInt(4), args.getInt(5));
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }

            // Actually update the byte array we're reading into
            for (int i = 0; i < byteArray.length; i++) {
                oopBytes.getFields().put(i, SimpleType.BYTE, byteArray[i]);
            }

            inflaterFields.putInt(2, (Integer) getFieldValue(inflater, "off"));
            inflaterFields.putInt(3, (Integer) getFieldValue(inflater, "len"));
            inflaterFields.putBoolean(4, (Boolean) getFieldValue(inflater, "finished"));
            inflaterFields.putBoolean(5, (Boolean) getFieldValue(inflater, "needDict"));

            return NativeReturn.forInt((Integer) ret);
        }
    };


    private static Method findInflaterMethod(String name) {
        return findMethod(Inflater.class, name, true);
    }

    private final MethodSignature methodSignature;

    private InflaterNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }

    @Override
    public String getClassName() {
        return "java/util/zip/Inflater";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
