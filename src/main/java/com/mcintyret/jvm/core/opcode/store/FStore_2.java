package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FStore_2 extends SingleWidthStore_2 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x45;
    }
}
