package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.Type;

public class Utils {

    public static OopArray newArray(Type type, int size) {
        return new OopArray(null, null, new int[size * type.getSimpleType().getWidth()], type);
    }

    public static void putField(WordStack stack, int[] fields, Field field) {
        int offset = field.getOffset();
        if (field.getType().getSimpleType().isDoubleWidth()) {
            int two = stack.pop();
            int one = stack.pop();
            fields[offset++] = one;
            fields[offset] = two;
        } else {
            fields[offset] = stack.pop();
        }
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

}
