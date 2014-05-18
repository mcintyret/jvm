package com.mcintyret.jvm.parse.cp;

public class CpFloat {

    private final int floatBits;

    public CpFloat(int floatBits) {
        this.floatBits = floatBits;
    }

    public int getFloatBits() {
        return floatBits;
    }
}
