package com.mcintyret.jvm.core.nativeimpls;

import com.google.common.collect.ImmutableSet;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.util.Utils;

import java.io.File;
import java.util.Set;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

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
    },
    LIST("list", "(Ljava/io/File;)[Ljava/lang/String;") {
        /*
          These directories contain jnilib files
          For some reason, if the real result is returned, the JVM tries to open the files as a ZipFile
          - since they aren't zip file, it blows up

          TOOD: work out why this is happening and fix it
        */

        private final Set<String> FORBIDDEN_FILES = ImmutableSet.of(
            "/Library/Java/Extensions",
            "/System/Library/Java/Extensions"
        );

        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            File file = makeCorrespondingFile(args);

            if (FORBIDDEN_FILES.contains(file.getAbsolutePath())) {
                return NativeReturn.forNull();
            }

            String[] res = file.list();

            if (res == null) {
                return NativeReturn.forNull();
            }

            OopArray array = Heap.allocateAndGet(new OopArray(ArrayClassObject.forType(ArrayType.create(NonArrayType.forClass(ImportantClasses.JAVA_LANG_STRING), 1)), new Variables(res.length)));

            for (int i = 0; i < res.length; i++) {
                array.getFields().getRawValues()[i] = Utils.toOopString(res[i]).getAddress();
            }

            return NativeReturn.forReference(array);
        }
    },
    GET_LAST_MODIFIED_TIME("getLastModifiedTime", "(Ljava/io/File;)J") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            File file = makeCorrespondingFile(args);

            return NativeReturn.forLong(file.lastModified());
        }
    };

    private static File makeCorrespondingFile(Variables args) {
        // These methods aren't static, so we need to take the second arg
        return new File(Utils.toString((OopClass) args.getOop(1).getFields().getOop(0)));
    }


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

