package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FStore_3 extends SingleWidthStore_3 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x46;
    }
}
