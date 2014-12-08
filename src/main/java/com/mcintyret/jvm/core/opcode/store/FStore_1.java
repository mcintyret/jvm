package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FStore_1 extends SingleWidthStore_1 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x44;
    }
}
