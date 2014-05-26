package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.NonArrayType;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Threads;

import java.io.IOException;

public class Runner {

    private static final MethodSignature MAIN_METHOD_SIGNATURE = MethodSignature.parse("main", "([Ljava/lang/String;)V");

    public void run(ClassPath classPath, String mainClassName, String... args) throws IOException {
        ClassLoader loader = ClassLoader.getDefaultClassLoader();

        loader.load(classPath);

        ClassObject mainClass = loader.getClassObject(mainClassName);

        Method mainMethod = findMainMethod(mainClass);

        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(NonArrayType.forClass("java/lang/String"), 1));
        OopArray array = aco.newArray(args.length);
        for (int i = 0; i < args.length; i++) {
            array.getFields()[i] = Heap.intern(args[i]);
        }
        int[] actualArgs = new int[]{Heap.allocate(array)};

        Thread main = makeMainThread(loader, mainMethod, actualArgs);

        NativeReturn ret = main.run();

        if (ret != null) {
            WordStack stack = new WordStack();
            ret.applyToStack(stack);
            try {
                int i = stack.pop();
                OopClass obj = Heap.getOopClass(i);
                if (obj.getClassObject().isInstanceOf(loader.getClassObject("java/lang/Throwable"))) {
                    System.out.println("Died with Exception of type: " + obj.getClassObject().getClassName());
                }

            } catch (Throwable foo) {
                // ignore
            }
        }

        System.out.println("DONE!!!");
    }

    private Thread makeMainThread(ClassLoader loader, Method mainMethod, int[] actualArgs) {
        ClassObject threadClass = loader.getClassObject("java/lang/Thread");
        OopClass theThread = threadClass.newObject();
        Method threadCtor = threadClass.findMethod("<init>", "(Ljava/lang/String;)V", false);
        int[] threadCtorArgs = threadCtor.newArgArray();
        threadCtorArgs[0] = Heap.allocate(theThread);
        threadCtorArgs[1] = Heap.intern("main");
        Utils.executeMethod(threadCtor, threadCtorArgs, null);

        Thread main = new Thread(theThread, "main", mainMethod, actualArgs);
        Threads.register(main);
        return main;
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
