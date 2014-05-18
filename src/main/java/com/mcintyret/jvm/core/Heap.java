package com.mcintyret.jvm.core;

import java.util.HashMap;
import java.util.Map;

public class Heap {

    private static final Oop[] OOP_TABLE = new Oop[1000];

    private static int heapAllocationPointer = 1;

    public static final int NULL_POINTER = 0;

    private static final StringPool STRING_POOL = new StringPool();

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
        oop.setAddress(heapAllocationPointer);
        return heapAllocationPointer++;
    }

    public static int intern(String string) {
        return STRING_POOL.intern(string);
    }

    private static class StringPool {

        private static final ClassObject STRING_CLASS = null; // TODO!

        private final Map<String, Oop> lookupMap = new HashMap<>();

        public int intern(String string) {
            // TODO: Actually create the string object!
            Oop oop = lookupMap.get(string);
            if (oop == null) {
                oop = STRING_CLASS.newObject();
                Heap.allocate(oop);
                lookupMap.put(string, oop);
            }
            return oop.getAddress();
        }
    }
}
