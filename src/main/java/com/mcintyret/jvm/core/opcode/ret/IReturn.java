package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IReturn extends SingleWidthReturn implements IntTyped {

    @Override
    public byte getByte() {
        return (byte) 0xAC;
    }
}
