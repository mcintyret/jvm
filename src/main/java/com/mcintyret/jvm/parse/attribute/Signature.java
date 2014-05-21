package com.mcintyret.jvm.parse.attribute;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class Signature extends Attribute {

    private final int signatureIndex;

    protected Signature(int signatureIndex) {
        super(AttributeType.SIGNATURE);
        this.signatureIndex = signatureIndex;
    }
}
