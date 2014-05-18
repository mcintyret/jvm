package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.domain.Type;

public class Utils {

    public static Field[] makeFields(Type[] types) {
        Field[] fields = new Field[types.length];
        int offset = 0;
        for (int i = 0; i < types.length; i++) {
            Type type = types[i];
            fields[i] = new Field(type, name, offset);
            offset += type.getSimpleType().isDoubleWidth() ? 2 : 1;
        }
        return fields;
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
