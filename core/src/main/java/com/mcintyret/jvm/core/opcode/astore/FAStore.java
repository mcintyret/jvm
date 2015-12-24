package com.mcintyret.jvm.core.opcode.astore;

import com.mcintyret.jvm.core.opcode.FloatTyped;

class FAStore extends SingleWidthAStore implements FloatTyped {

    @Override
    public byte getByte() {
        return 0x51;
    }
}
