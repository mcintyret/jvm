package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

abstract class TypeOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        WordStack stack = ctx.getStack();
        int address = stack.pop();
        ClassObject type = ctx.getConstantPool().getClassObject(ctx.getByteIterator().nextShort());
        if (address == Heap.NULL_POINTER) {
            handleNull(stack);
        } else {
            Oop oop = Heap.getOop(address);
            handleType(oop.getClassObject().isInstanceOf(type), stack);
        }
    }

    protected abstract void handleType(boolean instanceOf, WordStack stack);

    protected abstract void handleNull(WordStack stack);
}
