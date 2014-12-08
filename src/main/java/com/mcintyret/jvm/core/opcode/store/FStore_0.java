package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FStore_0 extends SingleWidthStore_0 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x43;
    }
}
