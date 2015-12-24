package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.IntTyped;

class ILoad_0 extends SingleWidthLoad_0 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x1A;
    }
}
