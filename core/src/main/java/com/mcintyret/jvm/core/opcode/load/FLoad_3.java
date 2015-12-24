package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FLoad_3 extends SingleWidthLoad_3 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x25;
    }
}
