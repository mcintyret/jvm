package com.mcintyret.jvm.core;

import java.nio.ByteBuffer;

public class ByteBufferIterator implements ByteIterator {

    private final ByteBuffer buffer;

    public ByteBufferIterator(byte[] bytes) {
        this(ByteBuffer.wrap(bytes));
    }

    public ByteBufferIterator(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public byte next() {
        return buffer.get();
    }

    @Override
    public int nextShort() {
        return buffer.getShort();
    }

    @Override
    public int nextInt() {
        return buffer.getInt();
    }

    @Override
    public boolean hasNext() {
        return buffer.hasRemaining();
    }

    @Override
    public void seek(int offset) {
        buffer.position(buffer.position() + offset);
    }

    @Override
    public byte[] nextBytes(int n) {
        byte[] bytes = new byte[n];
        buffer.get(bytes);
        return bytes;
    }
}
