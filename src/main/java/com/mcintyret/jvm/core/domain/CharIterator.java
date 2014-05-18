package com.mcintyret.jvm.core.domain;

import java.util.NoSuchElementException;

class CharIterator {

    private final CharSequence seq;

    int i = 0;

    CharIterator(CharSequence seq) {
        this.seq = seq;
    }

    char next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return seq.charAt(i++);
    }

    char peek() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return seq.charAt(i);
    }

    boolean hasNext() {
        return i != seq.length() - 1;
    }
}
