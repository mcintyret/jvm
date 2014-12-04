package com.mcintyret.jvm.core.opcode.load;

import com.mcintyret.jvm.core.util.ByteIterator;

abstract class SingleWidthLoad_3 extends SingleWidthLoad {
    
    @Override
    protected final int getIndex(ByteIterator bytes) {
        return 3;
    }
}
