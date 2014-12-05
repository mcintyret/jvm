package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.opcode.IntTyped;

class IStore_2 extends SingleWidthStore_2 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x3D;
    }
}
