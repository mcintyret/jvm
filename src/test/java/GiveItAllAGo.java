import com.mcintyret.jvm.core.*;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;
import java.io.IOException;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new DirectoryClassPath(System.getProperty("user.dir") + "/target/test-classes/java");

        String mainClass = "java/test/Main";

        NativeExecutionRegistry.registerNativeExecution(mainClass,
                MethodSignature.parse("print", "(Ljava/lang/String;)V"), localArgs -> {
                    System.out.println("NATIVE METHOD!!!: " + Utils.toString(Heap.getOop(localArgs[0])));
                    return NativeReturn.forVoid();
                });

        new Runner().run(classPath, mainClass);
    }

}
