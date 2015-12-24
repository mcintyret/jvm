package com.mcintyret.jvm.core.opcode.aload;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AALoad extends SingleWidthALoad implements RefTyped {

    @Override
    public byte getByte() {
        return 0x32;
    }

}
