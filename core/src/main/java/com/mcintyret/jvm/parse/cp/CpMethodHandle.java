package com.mcintyret.jvm.parse.cp;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class CpMethodHandle {

    private final byte referenceKind;

    private final int referenceIndex;

    public CpMethodHandle(byte referenceKind, int referenceIndex) {
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }
}
