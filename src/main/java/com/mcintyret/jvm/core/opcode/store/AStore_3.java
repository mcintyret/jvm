package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.RefTyped;

class AStore_3 extends SingleWidthStore_3 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x4E;
    }
}
