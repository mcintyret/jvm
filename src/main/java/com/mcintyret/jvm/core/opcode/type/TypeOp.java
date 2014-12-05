package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.type.SimpleType;

abstract class TypeOp extends OpCode {

    @Override
    public final void execute(OperationContext ctx) {
        VariableStack stack = ctx.getStack();
        int address = stack.popChecked(SimpleType.REF);
        AbstractClassObject type = ctx.getConstantPool().getClassObject(ctx.getByteIterator().nextShort());
        if (address == Heap.NULL_POINTER) {
            handleNull(stack);
        } else {
            Oop oop = Heap.getOop(address);
            handleType(oop.getClassObject().isInstanceOf(type), stack, address);
        }
    }

    protected abstract void handleType(boolean instanceOf, VariableStack stack, int address);

    protected abstract void handleNull(VariableStack stack);
}
