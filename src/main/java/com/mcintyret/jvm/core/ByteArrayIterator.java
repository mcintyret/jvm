package com.mcintyret.jvm.core;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ByteArrayIterator implements ByteIterator {

    private final byte[] bytes;

    private int i = 0;

    public ByteArrayIterator(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte next() {
        if (hasNext()) {
            return bytes[i++];
        }
        throw new NoSuchElementException();
    }

    @Override
    public int nextShort() {
        return Utils.toShort(next(), next());
    }

    @Override
    public int nextInt() {
        return Utils.toInt(next(), next(), next(), next());
    }

    @Override
    public boolean hasNext() {
        return i < bytes.length;
    }

    @Override
    public void seek(int offset) {
        i += offset;
    }

    @Override
    public byte[] nextBytes(int n) {
        byte[] ret = Arrays.copyOfRange(bytes, i, i + n);
        i += n;
        return ret;
    }
}
