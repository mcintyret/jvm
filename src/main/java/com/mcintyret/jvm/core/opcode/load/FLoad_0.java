package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FLoad_0 extends SingleWidthLoad_0 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x22;
    }
}
