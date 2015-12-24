package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AStore_1 extends SingleWidthStore_1 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x4C;
    }
}
