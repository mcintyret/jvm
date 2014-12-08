package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.IntTyped;

class ILoad_3 extends SingleWidthLoad_3 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x1D;
    }
}
