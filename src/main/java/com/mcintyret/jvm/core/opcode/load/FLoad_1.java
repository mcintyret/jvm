package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FLoad_1 extends SingleWidthLoad_1 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x23;
    }
}
