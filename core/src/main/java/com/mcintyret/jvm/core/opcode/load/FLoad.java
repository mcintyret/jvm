package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FLoad extends SingleWidthLoadIndexed implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x17;
    }
}
