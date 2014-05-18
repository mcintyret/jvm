package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class Code extends Attribute {

    private final int maxStack;

    private final int maxLocals;

    private final byte[] code;

    private final List<Exception> exceptions;

    private final List<Attribute> attributes;

    Code(int maxStack, int maxLocals, byte[] code, List<Exception> exceptions, List<Attribute> attributes) {
        super(AttributeType.CODE);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptions = exceptions;
        this.attributes = attributes;
    }

    public byte[] getCode() {
        return code;
    }
}
