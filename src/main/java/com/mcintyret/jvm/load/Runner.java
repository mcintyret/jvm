package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.ExecutionStack;
import com.mcintyret.jvm.core.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.domain.MethodSignature;
import java.io.IOException;

public class Runner {

    private static final MethodSignature MAIN_METHOD_SIGNATURE = MethodSignature.parse("main", "([Ljava/lang/String;)V");

    public void run(ClassPath classPath, String mainClassName, String... args) throws IOException {
        Loader loader = new Loader();

        loader.load(classPath);

        ClassObject mainClass = loader.getClassObject(mainClassName);

        Method mainMethod = findMainMethod(mainClass);

        int[] argRefs = new int[mainMethod.getMaxLocalVariables()];
        for (int i = 0; i < args.length; i++) {
            argRefs[i] = Heap.intern(args[i]);
        }

        ExecutionStack stack = new ExecutionStack();

        stack.push(new ExecutionStackElement(mainMethod.getByteCode(), argRefs, mainClass.getConstantPool(), stack));

        stack.execute();

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
