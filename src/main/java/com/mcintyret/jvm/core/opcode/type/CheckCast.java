package com.mcintyret.jvm.core.opcode.type;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.type.SimpleType;

class CheckCast extends TypeOp {

    @Override
    protected void handleType(boolean instanceOf, VariableStack stack, int address) {
        if (!instanceOf) {
            throw new ClassCastException();
        }
        stack.pushChecked(address, SimpleType.REF);
    }

    @Override
    protected void handleNull(VariableStack stack) {
        stack.pushChecked(Heap.NULL_POINTER, SimpleType.REF);
    }

    @Override
    public byte getByte() {
        return (byte) 0xC0;
    }
}
