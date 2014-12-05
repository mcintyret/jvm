package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.thread.Threads;
import com.mcintyret.jvm.core.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Heap {

    private static final int INITIAL_HEAP_SIZE = 32;

    private static final int MAX_HEAP_SIZE = 4096;

    private static volatile Oop[] OOP_TABLE = new Oop[INITIAL_HEAP_SIZE];

    private static AtomicInteger heapAllocationPointer = new AtomicInteger();

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

    public static void register() {
        GC_PHASER.register();
    }

    public static void deregister() {
        GC_PHASER.arriveAndDeregister();
    }

    public static void atSafePoint() {
        if (GC_PHASER.getArrivedParties() > 0) {
            GC_PHASER.arriveAndAwaitAdvance();
        }
    }

    private static final Phaser GC_PHASER = new Phaser() {
        @Override
        protected boolean onAdvance(int ignoredA, int ignoredB) {
            garbageCollection();

            return false;
        }

    };

    private static void garbageCollection() {
        // At this point we know that all the threads are awaiting the Phaser (!)

        // Optimistically assume we can reclaim enough resources that the existing heap size is big enough
        int index = 0;
        int[] newOops = new int[OOP_TABLE.length];

        for (Thread thread : Threads.getAll()) {
            for (ExecutionStackElement stack : thread.getExecutionStack().getStack()) {

            }
        }




    }

    public static int allocate(Oop oop) {
        int heapPointer = heapAllocationPointer.incrementAndGet();

        if (heapPointer >= OOP_TABLE.length) {
            // need to do some GCing!
            GC_PHASER.arriveAndAwaitAdvance();

            if (heapPointer >= MAX_HEAP_SIZE) {
                throw new OutOfMemoryError("No more heap space! Should probably do some kind of GC...");
            }
        }

        OOP_TABLE[heapPointer] = oop;
        oop.setAddress(heapPointer);
        return heapPointer;
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
