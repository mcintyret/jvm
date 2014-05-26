package com.mcintyret2.jvm.test;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
//        interfaceMethods();
//        simpleIntArrays();
        stringsAndNatives();
    }

    private static void interfaceMethods() {
        Foo foo = new FooBar();
        print(foo.doFoo());
    }

    private static void stringsAndNatives() {
        String str = "HELLO WORLD";

        String reversed = reverse(str);


        try {
            doFoo();
        } catch (FooBarException e) {
            print("Caught the exception!");
            print(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException();
        }
//        System.out.println(reversed);
    }

    private static void doFoo() throws FooBarException {
        throw new FooBarException();
    }

    private static void simpleIntArrays() {
        int[] array = new int[10];
        int a = 6;
        array[5] = 17;
        int res = array[a - 1] + a;

        char[] chars = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
    }

    private static String reverse(String in) {
        char[] chars = new char[in.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = in.charAt(in.length() - (i + 1));
        }
        return new String(chars);
    }

    private static native void print(String in);

    public static class FooBarException extends Exception {

    }
}
