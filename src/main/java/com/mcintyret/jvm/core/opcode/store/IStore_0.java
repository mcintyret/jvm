package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IStore_0 extends SingleWidthStore_0 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x3B;
    }
}
