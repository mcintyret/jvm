package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;

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

    public static String toString(Oop stringOop) {
        OopArray charArray = (OopArray) Heap.getOop(stringOop.getFields()[0]);

        char[] chars = new char[charArray.getLength()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) charArray.getFields()[i];
        }
        return new String(chars);
    }

    public static void executeMethod(Method method, int[] args) {
        ExecutionStack stack = new ExecutionStack();

        stack.push(new ExecutionStackElement(new ByteCode(method.getCode().getCode()), args,
            method.getClassObject().getConstantPool(), stack));

        stack.execute();
    }

}
