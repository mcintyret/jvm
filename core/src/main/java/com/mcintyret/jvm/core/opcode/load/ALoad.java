package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.RefTyped;

class ALoad extends SingleWidthLoadIndexed implements RefTyped {

    @Override
    public byte getByte() {
        return 0x19;
    }
}
