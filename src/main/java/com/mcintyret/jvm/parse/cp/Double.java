package com.mcintyret.jvm.parse.cp;

public class Double {

    private final int highBits;

    private final int lowBits;

    public Double(int highBits, int lowBits) {
        this.highBits = highBits;
        this.lowBits = lowBits;
    }
}
