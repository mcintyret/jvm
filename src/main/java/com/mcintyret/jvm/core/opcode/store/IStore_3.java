package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IStore_3 extends SingleWidthStore_3 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x3E;
    }
}
