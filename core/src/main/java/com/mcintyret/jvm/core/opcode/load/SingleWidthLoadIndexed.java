package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.util.ByteIterator;

abstract class SingleWidthLoadIndexed extends SingleWidthLoad {

    @Override
    protected final int getIndex(ByteIterator bytes) {
        return bytes.nextByte();
    }
}
