package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.RefTyped;

class ALoad_0 extends SingleWidthLoad_0 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x2A;
    }
}
