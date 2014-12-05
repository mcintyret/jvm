package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.opcode.IntTyped;

class ILoad extends SingleWidthLoadIndexed implements IntTyped {

    @Override
    public byte getByte() {
        return 0X15;
    }
}
