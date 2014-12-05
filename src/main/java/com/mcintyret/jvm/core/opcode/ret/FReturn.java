package com.mcintyret.jvm.core.opcode.ret;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FReturn extends SingleWidthReturn implements FloatTyped {

    @Override
    public byte getByte() {
        return (byte) 0xAE;
    }

}
