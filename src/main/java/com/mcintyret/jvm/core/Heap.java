package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import java.util.HashMap;
import java.util.Map;

public class Heap {

    private static ClassObject STRING_CLASS;

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

    public static OopClass getOopClass(int address) {
        return (OopClass) getOop(address);
    }

    public static OopArray getOopArray(int address) {
        return (OopArray) getOop(address);
    }


    public static int allocate(Oop oop) {
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
            // This is pretty fragile because if Sting ever changes this will need to change too

            if (STRING_CLASS == null) {
                STRING_CLASS = MagicClasses.getMagicClass("java/lang/String");
            }

            Oop stringOop = lookupMap.get(string);
            if (stringOop == null) {
                stringOop = STRING_CLASS.newObject();
                Heap.allocate(stringOop);

                int[] chars = new int[string.length()];
                for (int i = 0; i < string.length(); i++) {
                    chars[i] = string.charAt(i);
                }
                ArrayClassObject co = ArrayClassObject.forType(ArrayType.create(SimpleType.CHAR, 1));
                OopArray charArrayOop = new OopArray(co, chars);
                int charArrayAddress = Heap.allocate(charArrayOop);

                stringOop.getFields()[0] = charArrayAddress;
                // The only other field, hash, is initially 0 and so doesn't need changing

                lookupMap.put(string, stringOop);
            }
            return stringOop.getAddress();
        }
    }
}
