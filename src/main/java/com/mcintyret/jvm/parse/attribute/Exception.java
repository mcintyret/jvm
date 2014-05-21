package com.mcintyret.jvm.parse.attribute;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class Exception {

    public static final Parser<Exception> PARSER = bi -> new Exception(bi.nextShort());

    private final int exceptionClassIndex;

    public Exception(int exceptionClassIndex) {
        this.exceptionClassIndex = exceptionClassIndex;
    }
}
