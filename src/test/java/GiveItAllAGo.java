import java.io.IOException;

import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationAdapter;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new DirectoryClassPath(System.getProperty("user.dir") + "/target/test-classes/com/mcintyret2/jvm");

        String mainClass = "com/mcintyret2/jvm/test/Main";

        NativeImplementationRegistry.registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("print", "(Ljava/lang/String;)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                System.out.println("NATIVE METHOD!!!: " + Utils.toString(args.<OopClass>getOop(0)));
                return NativeReturn.forVoid();
            }
        });

        try {
            new Runner().run(classPath, mainClass);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("Total operations: " + ExecutionStackElement.TOTAL_OPCODES_EXECUTED.get());
            System.out.println("Current method: " + ExecutionStackElement.current.getMethod());
        }
    }
}
