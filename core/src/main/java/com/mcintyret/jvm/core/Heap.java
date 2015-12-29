package com.mcintyret.jvm.core;

import com.google.common.collect.Sets;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.Execution;
import com.mcintyret.jvm.core.exec.Thread;
import com.mcintyret.jvm.core.exec.Threads;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Heap {

    private static final Logger LOG = LoggerFactory.getLogger(Heap.class);

    private static final int INITIAL_HEAP_SIZE = 32;

    private static final int MAX_HEAP_SIZE = 200000;

    private static volatile Oop[] OOP_TABLE = new Oop[INITIAL_HEAP_SIZE];

    private static AtomicInteger heapAllocationPointer = new AtomicInteger();

    public static final int NULL_POINTER = 0;

    private static final StringPool STRING_POOL = new StringPool();

    private static final ConcurrentMap<java.lang.Thread, Stack<NativeMethodOops>> NATIVE_METHOD_OOPS = new ConcurrentHashMap<>();

    private static final Set<OopClassClass> OOP_CLASS_CLASSES = Collections.synchronizedSet(new HashSet<OopClassClass>());

    public static void enterNativeMethod() {
        NATIVE_METHOD_OOPS.get(currentThread()).push(new NativeMethodOops());
    }

    public static void exitNativeMethod() {
        NATIVE_METHOD_OOPS.get(currentThread()).pop();
    }

    private static java.lang.Thread currentThread() {
        return java.lang.Thread.currentThread();
    }

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

    public static void registerThread() {
        GC_PHASER.register();
        if (NATIVE_METHOD_OOPS.put(currentThread(), new Stack<>()) != null) {
            throw new IllegalStateException();
        }
    }

    public static void threadSleeping() {
        GC_PHASER.arriveAndDeregister();
    }

    public static void threadWaking() {
        // This will automatically wait for any ongoing GC, thanks to the semantics of Phaser.onAdvance()
        GC_PHASER.register();
    }

    public static void deregisterThread() {
        GC_PHASER.arriveAndDeregister();
        Stack<NativeMethodOops> stack = NATIVE_METHOD_OOPS.remove(currentThread());
        if (!stack.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    public static void atSafePoint() {
        if (GC_PHASER.getArrivedParties() > 0) {
            GC_PHASER.arriveAndAwaitAdvance();
        }
    }

    public static final Phaser GC_PHASER = new Phaser() {
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

    public static void registerNativeMethodArgs(Variables argArray) {
        Stack<NativeMethodOops> nativeMethodStack = NATIVE_METHOD_OOPS.get(currentThread());
        if (!nativeMethodStack.isEmpty()) {
            nativeMethodStack.peek().variables.add(argArray);
        }
    }

    private static class GarbageCollector {

        private static final float MAX_THRESHOLD = 0.85F;

        private final Map<Integer, Integer> newOopAddresses = new HashMap<>();

        private final int gcNum = GC_PHASER.getPhase();

        // Optimistically assume we can reclaim enough resources that the existing heap size is big enough
        private int index = 1; // 0 is reserved for NULL_POINTER
        private Oop[] newOops = new Oop[OOP_TABLE.length];

        public Oop[] run() {
            LOG.warn("Starting GC #{}, initial heap size: {}", gcNum, OOP_TABLE.length);

            OOP_CLASS_CLASSES.forEach(this::gcOop);

            // The String Pool
            // TODO: some kind of PERM_GEN so we don't have to do this every time
            for (Oop oop : STRING_POOL.lookupMap.values()) {
                gcOop(oop);
            }

            // All the execution stacks in all the live threads
            for (Thread thread : Threads.getAll()) {
                for (Execution exec : thread.getExecutions()) {
                    gcVariables(exec.getLocalVariables(), null);
                    gcVariables(exec.getStack().asVariables(), null);
                }
                gcOop(thread.getThisThread()); // Remember the thread itself
            }

            // Run for any objects currently being created in native methods
            NATIVE_METHOD_OOPS.computeIfPresent(currentThread(), (t, stack) -> {
                stack.forEach(nmp -> {
                    nmp.variables.forEach(v -> gcVariables(v, null));
                    nmp.oops.forEach(this::gcOop);
                });
                return stack;
            });

            // finally look at static objects
            for (ClassObject classObject : ClassLoader.getDefaultClassLoader().getLoadedClasses()) {
                gcOop(classObject.getOop(true)); // this will keep any reflection data.

                gcVariables(classObject.getStaticFieldValues(), null);
            }

            for (int i = 1; i < newOops.length; i++) {
                Oop oop = newOops[i];
                if (oop == null) {
                    break;
                }
                oop.getMarkRef().setLive(false); // reset for the next GC
            }

            if (index > MAX_THRESHOLD * newOops.length) {
                expand();
            }

            LOG.warn("Finished GC #{}, final heap size: {}", gcNum, index);

            heapAllocationPointer.set(index - 1);

            return newOops;
        }

        final Set<Integer> intersting = Sets.newHashSet(1740);

        private void gcVariables(Variables variables, Oop owner) {
            for (int i = 0; i < variables.length(); i++) {
                if (variables.getType(i) == SimpleType.REF) {
                    int address = variables.getRawValue(i);

                    if (address == NULL_POINTER) {
                        continue;
                    }

                    if (intersting.contains(address)) {
                        System.out.println("Oop at " + address + " is: " + getOop(address));
                    }

                    Integer newAddress = newOopAddresses.get(address);

                    if (newAddress != null) {
                        // we have already GC'd the Oop that this address pointed to and moved it to a new address.
                        // Update this reference and move on
                        variables.put(i, SimpleType.REF, newAddress);
                        continue;
                    }

                    Oop oop = variables.getOop(i);

                    gcOop(oop);

                    variables.put(i, SimpleType.REF, oop.getAddress()); // update the address
                } else if (variables.getType(i) == null && variables.getRawValue(i) != 0) {
                    throw new IllegalStateException("Untyped oop: " + getOop(variables.getRawValue(i)) + ", pos: " + i + ", owner: " + owner);
                }
            }
        }

        private void gcOop(Oop oop) {
            if (oop != null && !oop.getMarkRef().isLive()) {
                // we haven't visited this one yet
                oop.getMarkRef().setLive(true);

                // Actually keep the added Oop for the next cycle. This updates the Oop's address
                int oldAddress = oop.getAddress();

                addOop(oop);
                int newAddress = oop.getAddress();

                // Update the table before we recurse into this Oop's fields
                // - this ensures that circular references remain valid
                Integer oldAddressFromMap = newOopAddresses.put(oldAddress, newAddress);
                if (oldAddressFromMap != null) {
                    throw new IllegalStateException("Oop GCd multiple times");
                }

                gcVariables(oop.getFields(), oop);

            }
        }

        private void addOop(Oop oop) {
            if (index == newOops.length) {
                expand();
            }
            newOops[index] = oop;
            oop.setAddress(index);
            index++;
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
            if (heapPointer >= MAX_HEAP_SIZE) {
                throw new OutOfMemoryError("No more heap space! Should probably do some kind of GC...");
            }

            // need to do some GCing!
            GC_PHASER.arriveAndAwaitAdvance();

            // try again
            return allocate(oop);
        }

        OOP_TABLE[heapPointer] = oop;
        oop.setAddress(heapPointer);

        Stack<NativeMethodOops> nativeMethodStack = NATIVE_METHOD_OOPS.get(currentThread());
        if (!nativeMethodStack.isEmpty()) {
            nativeMethodStack.peek().oops.add(oop);
        }

        if (oop instanceof OopClassClass) {
            if (!OOP_CLASS_CLASSES.add((OopClassClass) oop)) {
                throw new IllegalStateException("Duplicate OopClassClass");
            }
        }

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

    private static class NativeMethodOops {

        private final Set<Oop> oops = new HashSet<>();

        private final Set<Variables> variables = new HashSet<>();

    }

}
