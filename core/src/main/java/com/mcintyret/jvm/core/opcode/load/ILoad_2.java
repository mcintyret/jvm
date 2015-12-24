package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.IntTyped;

class ILoad_2 extends SingleWidthLoad_2 implements IntTyped {

    @Override
    public byte getByte() {
        return 0x1C;
    }
}
