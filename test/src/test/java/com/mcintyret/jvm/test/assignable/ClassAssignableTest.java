package com.mcintyret.jvm.test.assignable;

import com.mcintyret.jvm.test.BaseJvmTest;
import com.mcintyret.jvm.test.TestOutput;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.testng.Assert.assertEquals;

@Test
public class ClassAssignableTest extends BaseJvmTest {

    @Override
    protected void verifyResults(TestOutput output) {
        assertEquals(output.getStdOut(), "true\n" +
            "false\n" +
            "true\n" +
            "true\n" +
            "true\n" +
            "false");
    }

    public static class Test {

        public static void main(String[] args) {
            System.out.println(List.class.isAssignableFrom(ArrayList.class));
            System.out.println(ArrayList.class.isAssignableFrom(List.class));
            System.out.println(List.class.isAssignableFrom(List.class));
            System.out.println(ArrayList.class.isAssignableFrom(ArrayList.class));

            System.out.println(new ArrayList<>() instanceof List);
            System.out.println(new HashSet<>() instanceof List);
        }
    }
}
