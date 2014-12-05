package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class Code extends Attribute {

    private final int maxStack;

    private final int maxLocals;

    private final byte[] code;

    private final List<CodeException> codeExceptions;

    private final Attributes attributes;

    Code(int maxStack, int maxLocals, byte[] code, List<CodeException> codeExceptions, Attributes attributes) {
        super(AttributeType.CODE);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.codeExceptions = codeExceptions;
        this.attributes = attributes;
    }

    public byte[] getCode() {
        return code;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public List<CodeException> getCodeExceptions() {
        return codeExceptions;
    }
}
