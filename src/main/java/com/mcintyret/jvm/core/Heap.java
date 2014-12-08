package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.exec.ExecutionStackElement;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Thread;
import com.mcintyret.jvm.core.thread.Threads;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Heap {

    private static final Logger LOG = LoggerFactory.getLogger(Heap.class);

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

        OOP_TABLE = new GarbageCollector().run();
    }

    private static class GarbageCollector {

        private static final float MAX_THRESHOLD = 0.85F;

        // Optimistically assume we can reclaim enough resources that the existing heap size is big enough
        private int index = 0;
        private Oop[] newOops = new Oop[OOP_TABLE.length];


        public Oop[] run() {
            LOG.warn("Starting GC, initial heap size: {}", OOP_TABLE.length);
            for (Thread thread : Threads.getAll()) {
                for (ExecutionStackElement stack : thread.getExecutionStack().getStack()) {
                    Variables localVariables = stack.getLocalVariables();
                    for (int i = 0; i < localVariables.length(); i++) {
                        if (localVariables.getType(i) == SimpleType.REF) {
                            Oop oop = localVariables.getOop(i);
                            gcOop(oop);
                            localVariables.putOop(i, oop); // update the address
                        }
                    }
                }
            }

            for (Oop oop : newOops) {
                if (oop == null) {
                    break;
                }
                oop.getMarkRef().setLive(false); // reset for the next GC
            }

            if (index > MAX_THRESHOLD * newOops.length) {
                expand();
            }

            LOG.warn("Finished GC, final heap size: {}", newOops.length);

            return newOops;
        }

        private void gcOop(Oop oop) {
            if (!oop.getMarkRef().isLive()) {
                // we haven't visited this one yet!
                oop.getMarkRef().setLive(true);

                // Actually keep the added Oop for the next cycle
                addOop(oop);

                int[] fieldVals = oop.getFields();

                Type type = oop.getClassObject().getType();
                if (type.isArray() && !((ArrayType) type).getComponentType().isPrimitive()) {
                    for (int i = 0; i < fieldVals.length; i++) {
                        Oop field = Heap.getOop(fieldVals[i]);
                        gcOop(field);
                        fieldVals[i] = field.getAddress(); // update the address
                    }
                } else if (!type.isArray()) {
                    Field[] fields = ((ClassObject) oop.getClassObject()).getInstanceFields();
                    int pos = 0;
                    for (Field field : fields) {
                        Type fieldType = field.getType();

                        if (!fieldType.isPrimitive()) {
                            Oop fieldOop = Heap.getOop(fieldVals[pos]);
                            gcOop(fieldOop);
                            fieldVals[pos] = oop.getAddress();
                        }
                        pos += fieldType.getWidth();
                    }
                }
            }
        }

        private void addOop(Oop oop) {
            if (index == newOops.length) {
                expand();
            }
            newOops[index] = oop;
            oop.setAddress(index);
        }

        private void expand() {
            Oop[] tmp = new Oop[newOops.length * 2];
            System.arraycopy(newOops, 0, tmp, 0, index);
            newOops = tmp;
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
