package com.mcintyret.jvm.core;

import java.util.BitSet;

public class ByteCode {

    private final byte[] bytes;

    private final BitSet isOpCode;

    public ByteCode(byte[] bytes, BitSet opCode) {
        this.bytes = bytes;
        isOpCode = opCode;
    }

    public ByteIterator byteIterator() {
        return new ByteBufferIterator(bytes);
    }
}
