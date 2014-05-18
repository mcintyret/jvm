package com.mcintyret.jvm.core;

public class ByteCode {

    private final byte[] bytes;

    public ByteCode(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteIterator byteIterator() {
        return new ByteBufferIterator(bytes);
    }
}
