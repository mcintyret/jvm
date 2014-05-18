package com.mcintyret.jvm.parse.cp;

public class Long {

    private final int highBits;

    private final int lowBits;

    public Long(int highBits, int lowBits) {
        this.highBits = highBits;
        this.lowBits = lowBits;
    }
}
