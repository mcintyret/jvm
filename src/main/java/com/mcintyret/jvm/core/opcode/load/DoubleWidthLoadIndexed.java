package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.ByteIterator;

abstract class DoubleWidthLoadIndexed extends DoubleWidthLoad {

    @Override
    protected final int getIndex(ByteIterator bytes) {
        return bytes.nextByte();
    }

}
