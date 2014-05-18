package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.ByteIterator;

abstract class DoubleWidthStore_3 extends DoubleWidthStore {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 3;
    }
}
