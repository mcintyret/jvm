package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.exec.WordStack;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.thread.Threads;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.util.Utils;

import java.io.IOException;

public class Runner {

    private static final MethodSignature MAIN_METHOD_SIGNATURE = MethodSignature.parse("main", "([Ljava/lang/String;)V");

    public static Thread MAIN_THREAD;

    public void run(ClassPath classPath, String mainClassName, String... args) throws IOException {
        ClassLoader loader = ClassLoader.getDefaultClassLoader();

        loader.load(classPath);

        // This happens early!
        MAIN_THREAD = createMainThread();

        loader.afterInitialLoad(); // Sets System.out. Can I do this anywhere else??

        ClassObject mainClass = loader.getClassObject(mainClassName);

        Method mainMethod = findMainMethod(mainClass);

        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(NonArrayType.forClass("java/lang/String"), 1));
        OopArray array = aco.newArray(args.length);
        for (int i = 0; i < args.length; i++) {
            array.getFields()[i] = Heap.intern(args[i]);
        }
        int[] actualArgs = new int[mainMethod.getCode().getMaxLocals()];
        actualArgs[0] = Heap.allocate(array);

        NativeReturn ret = Utils.executeMethodAndThrow(mainMethod, actualArgs, MAIN_THREAD);

        if (ret != null) {
            WordStack stack = new WordStack();
            ret.applyValue(stack);
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


    private static Method findMainMethod(ClassObject mainClass) {
        for (Method method : mainClass.getStaticMethods()) {
            if (method.getSignature().equals(MAIN_METHOD_SIGNATURE)) {
                return method;
            }
        }
        throw new IllegalStateException("No main method found on main class " + mainClass.getType());
    }


    private static Thread createMainThread() {
        ClassObject threadClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/Thread");
        ClassObject threadGroupClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/ThreadGroup");

        Method systemThreadGroupCtor = threadGroupClass.getDefaultConstructor();
        OopClass systemThreadGroup = threadGroupClass.newObject();

        int[] args = systemThreadGroupCtor.newArgArray();
        args[0] = Heap.allocate(systemThreadGroup);

        Utils.executeMethodAndThrow(systemThreadGroupCtor, args, null);

        // Cool, now we need the 'main' threadgroup...
        Method mainThreadGroupCtor =
            threadGroupClass.findConstructor("(Ljava/lang/Void;Ljava/lang/ThreadGroup;Ljava/lang/String;)V");
        OopClass mainThreadGroup = threadGroupClass.newObject();

        OopClass mainString = Heap.getOopClass(Heap.intern("main"));

        args = mainThreadGroupCtor.newArgArray();
        args[0] = Heap.allocate(mainThreadGroup);
        args[1] = Heap.NULL_POINTER;
        args[2] = systemThreadGroup.getAddress(); // parent
        args[3] = mainString.getAddress(); // name

        Utils.executeMethodAndThrow(mainThreadGroupCtor, args, null);

        // OK, now we can create the main thread
        OopClass mainThread = threadClass.newObject();

        // The pain here is that we can't run the constructor because that depends on Thread.currentThread(),
        // which in turn depends on having the main thread properly initialized!

        // are there more fields we care about?
        Field name = threadClass.findField("name", false);
        // Thread.name is a char array!
        name.set(mainThread, Heap.getOop(mainString.getFields()[0]));

        Field group = threadClass.findField("group", false);
        group.set(mainThread, mainThreadGroup);

        // This is a long!
        Field tid = threadClass.findField("tid", false);
        tid.set(mainThread, 0, 1);

        Field tidGen = threadClass.findField("threadSeqNumber", true);
        tidGen.set(null, 1L);

        Field priority = threadClass.findField("priority", false);
        priority.set(mainThread, 5);

        Field threadStatus = threadClass.findField("threadStatus", false);
        threadStatus.set(mainThread, 1); // I think this is RUNNABLE

        Thread main = new Thread(mainThread, java.lang.Thread.currentThread());
        Threads.register(main);
        return main;
    }
}
