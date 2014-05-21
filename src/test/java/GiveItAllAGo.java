import com.mcintyret.jvm.core.*;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.nativeimpls.NativeExecutionRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.load.*;

import java.io.IOException;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new AggregatingClassPath(
                new ZipClassPath("/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/rt.jar"),
                new DirectoryClassPath(System.getProperty("user.dir") + "/target/test-classes/com/mcintyret2/jvm"));

        String mainClass = "com/mcintyret2/jvm/test/Main";

        NativeExecutionRegistry.registerNativeExecution(mainClass,
                MethodSignature.parse("print", "(Ljava/lang/String;)V"), localArgs -> {
            System.out.println("NATIVE METHOD!!!: " + Utils.toString(Heap.getOop(localArgs[0])));
            return NativeReturn.forVoid();
        });

        new Runner().run(classPath, mainClass);
    }
}
