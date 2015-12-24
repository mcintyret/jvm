package com.mcintyret.jvm.core.util;

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
    public byte nextByte() {
        return buffer.get();
    }

    @Override
    public int nextByteUnsigned() {
        return 0xFF & nextByte();
    }

    @Override
    public short nextShort() {
        return buffer.getShort();
    }

    @Override
    public int nextShortUnsigned() {
        return 0xFFFF & nextShort();
    }

    @Override
    public int nextInt() {
        return buffer.getInt();
    }

    @Override
    public void seek(int offset) {
        buffer.position(buffer.position() + offset);
    }

    @Override
    public void setPos(int pos) {
        buffer.position(pos);
    }

    @Override
    public byte[] nextBytes(int n) {
        byte[] bytes = new byte[n];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    public int getPos() {
        return buffer.position();
    }
}
