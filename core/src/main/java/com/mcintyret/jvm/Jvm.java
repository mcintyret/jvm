package com.mcintyret.jvm;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationAdapter;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.AggregatingClassPath;
import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Jvm {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("No Main class given");
        }
        String mainClass = args[0].replaceAll("\\.", "/");
        String[] theArgs = new String[args.length - 1];
        if (theArgs.length > 0) {
            System.arraycopy(args, 1, theArgs, 0, theArgs.length);
        }

        ClassPath classPath;

        String classPathStr = System.getProperty("jvm.classpath");
        if (classPathStr == null) {
            classPath = ClassPath.emptyClasspath();
        } else {
            String[] classPaths = classPathStr.split(",\\s*");

            if (classPaths.length == 1) {
                classPath = new DirectoryClassPath(classPaths[0]);
            } else {
                List<ClassPath> classPathList = new ArrayList<>(classPaths.length);
                for (String cp : classPaths) {
                    classPathList.add(new DirectoryClassPath(cp));
                }
                classPath = new AggregatingClassPath(classPathList);
            }
        }

        installNativePrintMethod(mainClass);

        new Runner().run(classPath, mainClass);
    }

    private static void installNativePrintMethod(String mainClass) {
        NativeImplementationRegistry.registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("print", "(Ljava/lang/String;)V")) {
            @Override
            public NativeReturn execute(int[] args, OperationContext ctx) {
                System.out.println("NATIVE METHOD!!!: " + Utils.toString(Heap.getOopClass(args[0])));
                return NativeReturn.forVoid();
            }
        });
    }


}
