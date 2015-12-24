package com.mcintyret.jvm.parse.cp;

public class CpDouble {

    private final int highBits;

    private final int lowBits;

    public CpDouble(int highBits, int lowBits) {
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
