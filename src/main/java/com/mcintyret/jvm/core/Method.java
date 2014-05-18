package com.mcintyret.jvm.core;

public class Method {

    private final ByteCode byteCode;

    private final int argCount;

    public Method(ByteCode byteCode, int argCount) {
        this.byteCode = byteCode;
        this.argCount = argCount;
    }

    public ByteCode getByteCode() {
        return byteCode;
    }

    public int getArgCount() {
        return argCount;
    }
}
