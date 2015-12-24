package com.mcintyret.jvm.core.util;

public interface ByteIterator {

    byte nextByte();

    int nextByteUnsigned();

    short nextShort();

    int nextShortUnsigned();

    int nextInt();

    void seek(int offset);

    void setPos(int pos);

    byte[] nextBytes(int n);

    int getPos();

}
