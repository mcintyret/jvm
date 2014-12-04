package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.util.ByteIterator;

abstract class DoubleWidthStore_0 extends DoubleWidthStore {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 0;
    }
}
