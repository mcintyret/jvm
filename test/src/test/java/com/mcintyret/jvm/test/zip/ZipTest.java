package com.mcintyret.jvm.test.zip;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.testng.Assert.assertEquals;

@Test
public class ZipTest extends BaseJvmTest {
    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(output.getStdOut(), String.format("zipped.txt%nall zipped up%n%nzipped.txt%nall zipped up"));
    }

    public static class Test {
        public static void main(String[] args) throws IOException {
            File file = new File(System.getProperty("user.dir") + "/docs/zipped.zip");

            ZipFile zipFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> it = zipFile.entries();
            ZipEntry contents = it.nextElement();

            System.out.println(contents.getName());
            System.out.println(toString(zipFile.getInputStream(contents)));

            contents = zipFile.getEntry("zipped.txt");
            System.out.println(contents.getName());
            System.out.println(toString(zipFile.getInputStream(contents)));
        }

        private static String toString(InputStream stream) throws IOException {
            byte[] bytes = new byte[1024];
            int len = stream.read(bytes);
            stream.close();
            return new String(bytes, 0, len);
        }
    }
}
