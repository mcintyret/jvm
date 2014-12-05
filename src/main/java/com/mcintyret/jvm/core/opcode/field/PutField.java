package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.exec.VariableStack;
import com.mcintyret.jvm.core.oop.Oop;

class PutField extends Put {

    @Override
    public byte getByte() {
        return (byte) 0xB5;
    }

    @Override
    protected Oop getOop(VariableStack stack) {
        return stack.popOop();
    }
}
