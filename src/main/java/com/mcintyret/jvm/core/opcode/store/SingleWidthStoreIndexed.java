package com.mcintyret.jvm.core.opcode.store;

import com.mcintyret.jvm.core.ByteIterator;

abstract class SingleWidthStoreIndexed extends SingleWidthStore {

    @Override
    protected final int getIndex(ByteIterator bytes) {
        return bytes.next();
    }
}
