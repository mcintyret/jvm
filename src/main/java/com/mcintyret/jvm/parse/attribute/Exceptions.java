package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class Exceptions extends Attribute {

    private final List<Exception> exceptions;

    public Exceptions(int length, List<Exception> exceptions) {
        super(length);
        this.exceptions = exceptions;
    }
}
