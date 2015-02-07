package com.mcintyret2.jvm.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        interfaceMethods();
        simpleIntArrays();
        stringsAndNatives();
        simpleThreadingTest2();
        simpleThreadingTest();
        stringLength();
    }

    private static void stringLength() {
        print(String.valueOf("foo".length()));
    }

    private static void interfaceMethods() {
        Foo foo = new FooBar();
        print(foo.doFoo());
    }

    private static void stringsAndNatives() {
        String str = "HELLO WORLD";

        String reversed = reverse(str);

        print(reversed);
        System.out.println(reversed);
        System.err.println("Now I'm printing an error message!");

        try {
            doFoo();
        } catch (FooBarException e) {
            print("Caught the exception!");
            print(Arrays.toString(e.getStackTrace()));
//            throw new RuntimeException();
        }

        isAssignableFrom();
    }

    private static void simpleThreadingTest() {
        AtomicInteger ai = new AtomicInteger();
        int num = 40;
        final Random random = new Random();
        final CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100 + random.nextInt(50));
                    } catch (InterruptedException e) {
                        throw new AssertionError(e);
                    }
                    ai.getAndIncrement();
                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        System.out.println("Should be " + num + ": " + ai);
    }

    private static void simpleThreadingTest2() {
        final int[] a = new int[1];
        int num = 40;
        final Random random = new Random();
        final CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100 + random.nextInt(50));
                    } catch (InterruptedException e) {
                        throw new AssertionError(e);
                    }
                    synchronized (a) {
                        a[0]++;
                    }
                    latch.countDown();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        System.out.println("Should be " + num + ": " + a[0]);
    }

    private static void isAssignableFrom() {
        System.out.println("Should be true: " + Integer.class.isAssignableFrom(Number.class));
        System.out.println("Should be false: " + Integer.class.isAssignableFrom(String.class));
        System.out.println("Should be true: " + HashMap.class.isAssignableFrom(Map.class));
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
