package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AStore_2 extends SingleWidthStore_2 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x4D;
    }
}
