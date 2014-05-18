package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.ByteIterator;

abstract class SingleWidthStore_2 extends SingleWidthStore {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 2;
    }
}
