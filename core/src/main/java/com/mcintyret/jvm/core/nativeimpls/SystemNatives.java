package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassLoader;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum SystemNatives implements NativeImplementation {
    ARRAY_COPY("arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopArray src = args.getOop(0);
            OopArray dest = args.getOop(2);

            Type srcType = componentType(src);
            Type destType = componentType(dest);

            if (srcType.isPrimitive() && srcType != destType) {
                throw new ArrayStoreException("Type mismatch: " + srcType + " and " + destType);
            } else if (!srcType.isPrimitive()) {
                // TODO: move isInstanceOf around - might need to be a Type property?
            }

            int width = srcType.getWidth();

            int srcPos = args.getInt(1) * width;
            int destPos = args.getInt(3) * width;
            int len = args.getInt(4) * width;

            System.arraycopy(src.getFields().getRawValues(), srcPos, dest.getFields().getRawValues(), destPos, len);
            System.arraycopy(src.getFields().getTypes(), srcPos, dest.getFields().getTypes(), destPos, len);

            return NativeReturn.forVoid();
        }
    },
    CURRENT_TIME_MILLIS("currentTimeMillis", "()J") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forLong(System.currentTimeMillis());
        }
    },
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {

            // Already done
            return NativeReturn.forVoid();
        }
    },
    NANO_TIME("nanoTime", "()J") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forLong(System.nanoTime());
        }
    },
    IDENTITY_HASH_CODE("identityHashCode", "(Ljava/lang/Object;)I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // TODO: this is NOT how this works!
            return ObjectNatives.HASHCODE.execute(args, ctx);
        }
    },
    SET_OUT_0("setOut0", "(Ljava/io/PrintStream;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            Field out = system.findField("out", true);
            out.set(null, args.getRawValue(0));
            return NativeReturn.forVoid();
        }
    },
    SET_ERR_0("setErr0", "(Ljava/io/PrintStream;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            Field err = system.findField("err", true);
            err.set(null, args.getRawValue(0));
            return NativeReturn.forVoid();
        }
    },
    SET_IN_0("setIn0", "(Ljava/io/InputStream;)V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            ClassObject system = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/System");
            system.findField("in", true).set(null, args.getRawValue(0));
            return NativeReturn.forVoid();
        }
    },
    INIT_PROPERTIES("initProperties", "(Ljava/util/Properties;)Ljava/util/Properties;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopClass props = args.getOop(0);
            Method setProperty = props.getClassObject().findMethod("setProperty", false);

            for (String key : System.getProperties().stringPropertyNames()) {
                Variables spArgs = setProperty.newArgArray();
                spArgs.putOop(0, props);
                spArgs.put(1, SimpleType.REF, Heap.intern(key));
                spArgs.put(2, SimpleType.REF, Heap.intern(OVERRIDE_PROPERTIES.getOrDefault(key, System.getProperty(key))));

                Utils.executeMethodAndThrow(setProperty, spArgs, ctx.getThread());
            }

            return NativeReturn.forReference(props);
        }
    },
    MAP_LIBRARY_NAME("mapLibraryName", "(Ljava/lang/String;)Ljava/lang/String;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(
                Heap.intern(
                    System.mapLibraryName(Utils.toString(args.<OopClass>getOop(0)))));
        }
    };

    private static final Map<String, String> OVERRIDE_PROPERTIES = new HashMap<>();

    static {
        final String javaHome = System.getProperty("java.jvm.home");
        final String javaHomeJre = javaHome + "/jre";

        OVERRIDE_PROPERTIES.put("java.home", javaHomeJre);
        OVERRIDE_PROPERTIES.put("sun.boot.library.path", javaHomeJre + "/lib");
        OVERRIDE_PROPERTIES.put("sun.boot.class.path", javaHomeJre + "/lib/resources.jar:" +
            javaHomeJre + "/lib/rt.jar:" +
            javaHomeJre + "/lib/jsse.jar:" +
            javaHomeJre + "/lib/jce.jar:" +
            javaHomeJre + "/lib/charsets.jar:" +
            javaHomeJre + "/lib/ext/localedata.jar:" +
            javaHomeJre + "/lib/jfr.jar");
        OVERRIDE_PROPERTIES.put("java.vm.specification.version", "1.7");
        OVERRIDE_PROPERTIES.put("java.endorsed.dirs", javaHomeJre + "/lib/endorsed");
        OVERRIDE_PROPERTIES.put("java.class.version", "51.0");
        OVERRIDE_PROPERTIES.put("java.runtime.version", "1.7.0_51-b13");
        OVERRIDE_PROPERTIES.put("java.version", "1.7.0_51");

        // TODO: add non-core classpath elements to this
        Path javaHomeJrePath = Paths.get(javaHomeJre);
        Path javaHomeLibPath = Paths.get(javaHome + "/lib");
        final List<String> javaJars = getClassPath(Integer.MAX_VALUE, javaHomeJrePath);
        javaJars.addAll(getClassPath(1, javaHomeLibPath));

        StringBuilder classpath = new StringBuilder();
        Iterator<String> jarIt = javaJars.iterator();
        while (jarIt.hasNext()) {
            classpath.append(jarIt.next());
            if (jarIt.hasNext()) {
                classpath.append(":");
            }
        }

        OVERRIDE_PROPERTIES.put("java.class.path", System.getProperty("user.dir") + "/docs:" + classpath);
    }

    private static List<String> getClassPath(int depth, Path classPathRoot) {
        final List<String> javaJars = new ArrayList<>();
        try {
            Files.walkFileTree(classPathRoot, Collections.emptySet(), depth, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String name = file.toAbsolutePath().toString();
                    if (name.endsWith(".jar") && !name.endsWith("alt-rt.jar")) { // don't want the alt one - confuses things!
                        javaJars.add(name);
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return javaJars;
    }

    private final MethodSignature methodSignature;

    private SystemNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return "java/lang/System";
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }

    private static Type componentType(OopArray array) {
        return array.getClassObject().getType().getComponentType();
    }
}