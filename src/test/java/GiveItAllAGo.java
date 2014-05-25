import com.mcintyret.jvm.core.*;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationAdapter;
import com.mcintyret.jvm.core.nativeimpls.NativeImplemntationRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.load.*;

import java.io.IOException;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new AggregatingClassPath(
                new ZipClassPath("/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/rt.jar"),
                new DirectoryClassPath(System.getProperty("user.dir") + "/target/test-classes/com/mcintyret2/jvm"));

        String mainClass = "com/mcintyret2/jvm/test/Main";

        NativeImplemntationRegistry.registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("print", "(Ljava/lang/String;)V")) {
            @Override
            public NativeReturn execute(int[] args) {
                System.out.println("NATIVE METHOD!!!: " + Utils.toString(Heap.getOop(args[0])));
                return NativeReturn.forVoid();
            }
        });

        try {
            new Runner().run(classPath, mainClass);
        } finally {
            System.out.println("Total operations: " + ExecutionStackElement.TOTAL_OPCODES_EXECUTED.get());
        }
    }
}
