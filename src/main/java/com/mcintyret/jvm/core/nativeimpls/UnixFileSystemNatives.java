package com.mcintyret.jvm.core.nativeimpls;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

import java.io.File;

import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;

/**
 * User: tommcintyre
 * Date: 6/28/14
 */
public enum UnixFileSystemNatives implements NativeImplementation {
    INIT_IDS("initIDs", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    GET_BOOLEAN_ATTRIBUTES_0("getBooleanAttributes0", "(Ljava/io/File;)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Field filePath = getDefaultClassLoader().getClassObject("java/io/File").findField("path", false);

            String path = Utils.toString(filePath.getInt(args.getOop(1)));

            File file = new File(path);

            if (!file.exists()) {
                return NativeReturn.forInt(0);
            } else {
                int attrs = 1;
                // TODO: what about BA_REGULAR??
                if (file.isDirectory()) {
                    attrs |= 4;
                }

                return NativeReturn.forInt(attrs);
            }
        }
    },
    CANONICALIZE_0("canonicalize0", "(Ljava/lang/String;)Ljava/lang/String;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(args.getRawValue(1)); // Just return the input
        }
    };


    private final MethodSignature methodSignature;

    private UnixFileSystemNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/io/UnixFileSystem";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

}

