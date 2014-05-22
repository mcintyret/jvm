package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.nativeimpls.NativeImplemntationRegistry;
import com.mcintyret.jvm.core.nativeimpls.ObjectNatives;
import com.mcintyret.jvm.core.oop.OopArray;
import java.io.IOException;

public class Runner {

    private static final MethodSignature MAIN_METHOD_SIGNATURE = MethodSignature.parse("main", "([Ljava/lang/String;)V");

    static {
        NativeImplemntationRegistry.registerNative(ObjectNatives.REGISTER_NATIVES);
    }

    public void run(ClassPath classPath, String mainClassName, String... args) throws IOException {
        ClassLoader loader = ClassLoader.DEFAULT_CLASSLOADER;

        loader.load(classPath);

        ClassObject mainClass = loader.getClassObject(mainClassName);

        Method mainMethod = findMainMethod(mainClass);

        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(ReferenceType.forClass("java/lang/String"), 1));
        OopArray array = aco.newArray(args.length);
        for (int i = 0; i < args.length; i++) {
            array.getFields()[i] = Heap.intern(args[i]);
        }
        int[] actualArgs = new int[]{Heap.allocate(array)};

        Utils.executeMethod(mainMethod, actualArgs);

        System.out.println("DONE!!!");
    }

    private Method findMainMethod(ClassObject mainClass) {
        for (Method method : mainClass.getStaticMethods()) {
            if (method.getSignature().equals(MAIN_METHOD_SIGNATURE)) {
                return method;
            }
        }
        throw new IllegalStateException("No main method found on main class " + mainClass.getType());
    }
}
