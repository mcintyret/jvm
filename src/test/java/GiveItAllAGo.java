import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationAdapter;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;

import java.io.IOException;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new DirectoryClassPath(System.getProperty("user.dir") + "/target/test-classes/com/mcintyret2/jvm");

        String mainClass = "com/mcintyret2/jvm/test/Main";

        NativeImplementationRegistry.registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("print", "(Ljava/lang/String;)V")) {
            @Override
            public NativeReturn execute(Variable[] args, OperationContext ctx) {
                System.out.println("NATIVE METHOD!!!: " + Utils.toString(Heap.getOopClass(args[0])));
                return NativeReturn.forVoid();
            }
        });

        try {
            new Runner().run(classPath, mainClass);
        } finally {
            System.out.println("Total operations: " + ExecutionStackElement.TOTAL_OPCODES_EXECUTED.get());
            System.out.println("Current method: " + ExecutionStackElement.current.getMethod());
        }
    }
}
