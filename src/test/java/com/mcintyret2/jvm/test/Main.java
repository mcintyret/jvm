package com.mcintyret2.jvm.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
//        interfaceMethods();
//        simpleIntArrays();
//        stringsAndNatives();
//        threadingTests();
        threadingTestSynchronizedStaticMethodThrowingException();
//        stringLength();
    }

    private static void threadingTests() {
        threadingTestAtomicInteger();
        threadingTestSynchronizedBlock();
        threadingTestSynchronizedStaticMethod();
        threadingTestSynchronizedInstanceMethod();
        threadingTestSynchronizedBlockThrowingException();
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

    private static void doSimpleThreadingTest(int num, Runnable action) {
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
                    try {
                        action.run();
                    } finally {
                        latch.countDown();
                    }
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }

    // Test threading using an AtomicInteger
    private static void threadingTestAtomicInteger() {
        int num = 40;
        AtomicInteger ai = new AtomicInteger();
        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                ai.incrementAndGet();
            }
        });
        System.out.println("Should be " + num + ": " + ai);
    }

    // Test threading using a synchronized block
    private static void threadingTestSynchronizedBlock() {
        final int[] a = new int[1];
        int num = 40;

        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    a[0]++;
                }
            }
        });

        System.out.println("Should be " + num + ": " + a[0]);
    }

    // Test threading using a synchronized static method
    private static void threadingTestSynchronizedStaticMethod() {
        final int[] a = new int[1];
        int num = 40;

        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                incrementStatic(a);
            }
        });

        System.out.println("Should be " + num + ": " + a[0]);
    }


    // Test threading using a synchronized instance method
    private static void threadingTestSynchronizedInstanceMethod() {
        final int[] a = new int[1];
        int num = 40;

        class Sync {
            synchronized void incrementFirst(int[] a) {
                a[0]++;
            }
        }

        Sync sync = new Sync();

        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                sync.incrementFirst(a);
            }
        });

        System.out.println("Should be " + num + ": " + a[0]);
    }

    // Test threading using a synchronized block and throwing an exception
    private static void threadingTestSynchronizedBlockThrowingException() {
        final int[] a = new int[2];
        int num = 40;

        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    incrementExceptionNotSynchronized(a);
                }
            }
        });

        System.out.println("Should be " + num + ": " + a[0]);
        System.out.println("Should be " + (num / 2) + ": " + a[1]);
    }

    // Test threading using a synchronized static method and throwing an exception
    private static void threadingTestSynchronizedStaticMethodThrowingException() {
        final int[] a = new int[2];
        int num = 40;

        doSimpleThreadingTest(num, new Runnable() {
            @Override
            public void run() {
                incrementStaticException(a);
            }
        });

        System.out.println("Should be " + num + ": " + a[0]);
        System.out.println("Should be " + (num / 2) + ": " + a[1]);
    }

    private static synchronized void incrementStatic(int[] a) {
        a[0]++;
    }

    private static synchronized void incrementStaticException(int[] a) {
        incrementExceptionNotSynchronized(a);
    }

    private static void incrementExceptionNotSynchronized(int[] a) {
        if (a[0]++ % 2 == 0) {
            // Testing whether we give up the lock on exiting via an exception
            throw new RuntimeException();
        }
        a[1]++;
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
