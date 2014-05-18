package com.mcintyret.jvm.core.opcode.put;

import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Oop;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

import static com.mcintyret.jvm.core.Utils.putField;

public class PutField extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        Oop oop = Heap.getOop(stack.pop());

        FieldReference fieldRef = (FieldReference) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());
        Field field = fieldRef.getInstanceField();

        putField(stack, oop.getFields(), field);
    }



    @Override
    public byte getByte() {
        return (byte) 0xB5;
    }
}
