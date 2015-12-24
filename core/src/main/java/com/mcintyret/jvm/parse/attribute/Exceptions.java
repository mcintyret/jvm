package com.mcintyret.jvm.parse.attribute;

import java.util.List;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class Exceptions extends Attribute {

    private final List<Exception> exceptions;

    protected Exceptions(List<Exception> exceptions) {
        super(AttributeType.EXCEPTIONS);
        this.exceptions = exceptions;
    }
}
