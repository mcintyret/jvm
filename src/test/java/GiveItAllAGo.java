import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;
import java.io.IOException;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new DirectoryClassPath("/Users/mcintyret2/Github/jvm/target/test-classes/java/lang");

        String mainClass = "java/lang/Main";

        new Runner().run(classPath, mainClass);
    }

}
