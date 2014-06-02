package com.mcintyret.jvm.core.opcode.field;

import com.mcintyret.jvm.core.WordStack;
import com.mcintyret.jvm.core.oop.Oop;

class PutStatic extends Put {

    @Override
    public byte getByte() {
        return (byte) 0xB3;
    }

    @Override
    protected Oop getOop(WordStack stack) {
        return null;
    }
}
