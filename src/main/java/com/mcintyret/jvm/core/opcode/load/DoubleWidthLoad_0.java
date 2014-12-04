package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.util.ByteIterator;

abstract class DoubleWidthLoad_0 extends DoubleWidthLoad {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 0;
    }
}
