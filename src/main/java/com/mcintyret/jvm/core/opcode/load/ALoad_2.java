package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.RefTyped;

class ALoad_2 extends SingleWidthLoad_2 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x2C;
    }
}
