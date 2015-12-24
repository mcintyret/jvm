package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FLoad_2 extends SingleWidthLoad_2 implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x24;
    }
}
