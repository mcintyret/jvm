package com.mcintyret.jvm.parse.attribute;

public class LineNumber {

    public static final Parser<LineNumber> PARSER = bi -> new LineNumber(bi.nextShort(), bi.nextShort());

    private final int startPc;

    private final int lineNumber;

    public LineNumber(int startPc, int lineNumber) {
        this.startPc = startPc;
        this.lineNumber = lineNumber;
    }
}
