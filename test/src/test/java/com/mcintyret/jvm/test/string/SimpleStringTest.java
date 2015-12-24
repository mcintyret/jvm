package com.mcintyret.jvm.test.string;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;

import static org.testng.Assert.assertEquals;

public class SimpleStringTest extends BaseJvmTest {

    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(output.getStdOut(), "DLROW OLLEH");
    }

    public static class Test {

        public static void main(String[] args) {
            String str = "HELLO WORLD";

            String reversed = reverse(str);

            System.out.println(reversed);
        }

        private static String reverse(String in) {
            char[] chars = new char[in.length()];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = in.charAt(in.length() - (i + 1));
            }
            return new String(chars);
        }
    }

}
