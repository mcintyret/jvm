package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.RefTyped;

class ALoad_1 extends SingleWidthLoad_1 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x2B;
    }
}
