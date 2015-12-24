package com.mcintyret.jvm.parse.attribute;

public class CodeException {

    public static final Parser<CodeException> PARSER = bi -> new CodeException(bi.nextShort(), bi.nextShort(), bi.nextShort(), bi.nextShort());

    private final int startPc;

    private final int endPc;

    private final int handlerPc;

    private final int catchType;

    private CodeException(int startPc, int endPc, int handlerPc, int catchType) {
        this.startPc = startPc;
        this.endPc = endPc;
        this.handlerPc = handlerPc;
        this.catchType = catchType;
    }

    public int getStartPc() {
        return startPc;
    }

    public int getEndPc() {
        return endPc;
    }

    public int getHandlerPc() {
        return handlerPc;
    }

    public int getCatchType() {
        return catchType;
    }
}
