package com.mcintyret.jvm.parse.attribute;

public class Exception {

    public static final Parser<Exception> PARSER = bi -> new Exception(bi.nextShort(), bi.nextShort(), bi.nextShort(), bi.nextShort());

    private final int startPc;

    private final int endPc;

    private final int handlerPc;

    private final int catchType;

    private Exception(int startPc, int endPc, int handlerPc, int catchType) {
        this.startPc = startPc;
        this.endPc = endPc;
        this.handlerPc = handlerPc;
        this.catchType = catchType;
    }
}
