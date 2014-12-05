package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IStore_1 extends SingleWidthStore_1 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x3C;
    }
}
