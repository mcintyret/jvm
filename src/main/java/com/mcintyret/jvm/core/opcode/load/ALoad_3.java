package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.RefTyped;

class ALoad_3 extends SingleWidthLoad_3 implements RefTyped {

    @Override
    public byte getByte() {
        return 0x2D;
    }
}
