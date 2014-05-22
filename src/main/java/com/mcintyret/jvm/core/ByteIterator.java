package com.mcintyret.jvm.core;

public interface ByteIterator {

    byte nextByte();

    int nextByteUnsigned();

    short nextShort();

    int nextShortUnsigned();

    int nextInt();

    void seek(int offset);

    byte[] nextBytes(int n);

}
