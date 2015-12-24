package com.mcintyret.jvm;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
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
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Jvm {

    public static void main(String[] args) throws IOException {

        setLogLevel();

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

        boolean error = false;
        try {
            new Runner().run(classPath, mainClass);
        } catch (Throwable t) {
            error = true;
            throw t;
        } finally {
            if (error) {
                System.err.println("Total operations: " + ExecutionStackElement.TOTAL_OPCODES_EXECUTED.get());
                System.err.println("Current method: " + ExecutionStackElement.current.getMethod());
            }
        }
    }

    private static void setLogLevel() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger("ROOT");
        String level = System.getProperty("jvm.logLevel", "OFF");
        rootLogger.setLevel(Level.toLevel(level));
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
