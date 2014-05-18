package com.mcintyret.jvm.core;

public interface ByteIterator {

    byte next();

    int nextShort();

    int nextInt();

    boolean hasNext();

    void seek(int offset);

    byte[] nextBytes(int n);

}
