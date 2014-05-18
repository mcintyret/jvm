package com.mcintyret.jvm.parse.cp;

public class CpLong {

    private final int highBits;

    private final int lowBits;

    public CpLong(int highBits, int lowBits) {
        this.highBits = highBits;
        this.lowBits = lowBits;
    }

    public int getHighBits() {
        return highBits;
    }

    public int getLowBits() {
        return lowBits;
    }
}
