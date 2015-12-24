package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class Heap {

    private static final int HEAP_SIZE = 3000;

    private static final Oop[] OOP_TABLE = new Oop[HEAP_SIZE];

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

    public static OopClass getOopClass(int address) {
        return (OopClass) getOop(address);
    }

    public static OopArray getOopArray(int address) {
        return (OopArray) getOop(address);
    }


    public static int allocate(Oop oop) {
        if (heapAllocationPointer >= OOP_TABLE.length) {
            throw new OutOfMemoryError("No more heap space! Should probably do some kind of GC...");
        }
        OOP_TABLE[heapAllocationPointer] = oop;
        oop.setAddress(heapAllocationPointer);
        return heapAllocationPointer++;
    }

    public static <O extends Oop> O allocateAndGet(O oop) {
        allocate(oop);
        return oop;
    }

    public static int intern(String string) {
        return STRING_POOL.intern(string);
    }

    private static class StringPool {

        private final Map<String, Oop> lookupMap = new HashMap<>();

        public int intern(String string) {

            Oop stringOop = lookupMap.get(string);
            if (stringOop == null) {
                stringOop = Utils.toOopString(string);
                lookupMap.put(string, stringOop);
            }
            return stringOop.getAddress();
        }
    }
}
