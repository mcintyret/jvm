package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Thread;

public class Utils {

    public static OopArray newArray(Type type, int size) {
        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(type, 1));
        return new OopArray(aco, new int[size * type.getSimpleType().getWidth()]);
    }

    public static void getField(WordStack stack, int[] fields, Field field) {
        int offset = field.getOffset();
        if (field.getType().getSimpleType().isDoubleWidth()) {
            int one = fields[offset++];
            int two = fields[offset];
            stack.push(one);
            stack.push(two);
        } else {
            stack.push(fields[offset]);
        }
    }

    public static long toLong(int l, int r) {
        return (long) l << 32 | r & 0xFFFFFFFFL;
    }

    public static String toString(OopClass stringOop) {
        return toString((OopArray) Heap.getOop(stringOop.getFields()[0]));
    }

    public static String toString(OopArray charArrayOop) {
        char[] chars = new char[charArrayOop.getLength()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) charArrayOop.getFields()[i];
        }
        return new String(chars);
    }

    public static NativeReturn executeMethod(Method method, int[] args, Thread thread) {
        ExecutionStack stack = new ExecutionStack(thread);

        stack.push(new ExecutionStackElement(method, args,
                method.getClassObject().getConstantPool(), stack));

        stack.execute();

        return stack.getFinalReturn();
    }

}
