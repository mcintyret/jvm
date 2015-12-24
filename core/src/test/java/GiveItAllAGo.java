import com.mcintyret.jvm.core.exec.Execution;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationAdapter;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassPath;
import com.mcintyret.jvm.load.DirectoryClassPath;
import com.mcintyret.jvm.load.Runner;

import java.io.IOException;

import static com.mcintyret.jvm.core.nativeimpls.NativeImplementationRegistry.registerNative;

public class GiveItAllAGo {

    public static void main(String[] args) throws IOException {
        ClassPath classPath = new DirectoryClassPath(System.getProperty("user.dir") + "/core/target/test-classes/com/mcintyret2/jvm");

        String mainClass = "com/mcintyret2/jvm/test/Main";

        registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("print", "(Ljava/lang/String;)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                System.out.println("NATIVE METHOD!!!: " + Utils.toString(args.<OopClass>getOop(0)));
                return NativeReturn.forVoid();
            }
        });

        registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("incrementNativeInstance", "([I)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                OopArray array = args.getOop(1);
                array.getFields()[0]++;
                return NativeReturn.forVoid();
            }
        });

        registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("incrementNativeStatic", "([I)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                OopArray array = args.getOop(0);
                array.getFields()[0]++;
                return NativeReturn.forVoid();
            }
        });

        registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("incrementNativeInstanceException", "([I)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                OopArray array = args.getOop(1);
                if (array.getFields()[0]++ % 2 == 0) {
                    return NativeReturn.forThrowable(Utils.toThrowableOop(new RuntimeException(), ctx.getThread()));
                }
                array.getFields()[1]++;
                return NativeReturn.forVoid();
            }
        });

        registerNative(new NativeImplementationAdapter(mainClass, MethodSignature.parse("incrementNativeStaticException", "([I)V")) {
            @Override
            public NativeReturn execute(Variables args, OperationContext ctx) {
                OopArray array = args.getOop(0);
                if (array.getFields()[0]++ % 2 == 0) {
                    return NativeReturn.forThrowable(Utils.toThrowableOop(new RuntimeException(), ctx.getThread()));
                }
                array.getFields()[1]++;
                return NativeReturn.forVoid();
            }
        });

        try {
            new Runner().run(classPath, mainClass);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("Total operations: " + Execution.TOTAL_OPCODES_EXECUTED.get());
        }
    }
}
