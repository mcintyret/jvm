package com.mcintyret.jvm.test.fileread;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;

public class ReadFileTest extends BaseJvmTest {
    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(output.getStdOut(), "This is only a test");
    }

    public static class Test {

        public static void main(String[] args) throws IOException {
            File file = new File(System.getProperty("user.dir") + "/docs/testFile.txt");
            InputStream stream = new FileInputStream(file);

            byte[] bytes = new byte[1024];
            stream.read(bytes);
            stream.close();

            System.out.println(new String(bytes));
        }
    }
}
