package com.mcintyret.jvm.core.opcode.put;

import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Oop;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

public class PutField extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        FieldReference fieldRef = (FieldReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());
        Field field = fieldRef.getInstanceField();

        putField(ctx.getStack(), field);
    }

    private static void putField(WordStack stack, Field field) {
        if (field.getType().getSimpleType().isDoubleWidth()) {
            putDoubleWidthField(stack, field);
        } else {
            putSingleWidthField(stack, field);
        }
    }

    private static void putDoubleWidthField(WordStack stack, Field field) {
        int offset = field.getOffset();
        int two = stack.pop();
        int one = stack.pop();
        Oop oop = Heap.getOop(stack.pop());
        int[] fields = oop.getFields();
        fields[offset++] = one;
        fields[offset] = two;
    }

    private static void putSingleWidthField(WordStack stack, Field field) {
        int val = stack.pop();
        Heap.getOop(stack.pop()).getFields()[field.getOffset()] = val;
    }


    @Override
    public byte getByte() {
        return (byte) 0xB5;
    }
}
