package com.mcintyret.jvm.core;

public class Heap {
    // static for now - this is shit, fix it!

    private static final Oop[] OOP_TABLE = new Oop[1000];

    private static int heapAllocationPointer = 1;

    public static final int NULL_POINTER = 0;

    private static final int TEST = 235792579;

    private static final long TEST_LONG = 23579257900000L;

    private static final String TEST_STRING = "foo bar baz";

    public static Oop getOop(int address) {
        if (address == NULL_POINTER) {
            return null;
        }
        Oop oop = OOP_TABLE[address];
        if (oop == null) {
            throw new IllegalArgumentException("No Oop found at address " + address);
        }
        return oop;
    }

    public static int allocate(Oop oop) {
        OOP_TABLE[heapAllocationPointer] = oop;
        return heapAllocationPointer++;
    }

    public static boolean isTest(int a) {
        return a == TEST;
    }

    public static boolean isTestLong(long l) {
        return l == TEST_LONG;
    }

    public static boolean isTestString(String str) {
        return str == TEST_STRING;
    }

}
