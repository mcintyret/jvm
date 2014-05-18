package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.ByteIterator;

abstract class DoubleWidthLoad_1 extends DoubleWidthLoad {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 1;
    }
}
