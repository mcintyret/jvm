package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.IntTyped;

class ILoad_1 extends SingleWidthLoad_1 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x1B;
    }
}
