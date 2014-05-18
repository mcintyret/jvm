package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class Exceptions extends Attribute {

    private final List<Exception> exceptions;

    public Exceptions(List<Exception> exceptions) {
        super(AttributeType.EXCEPTIONS);
        this.exceptions = exceptions;
    }
}
