package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.Parser;

public class AnnotationNameAndValue {

    public static final Parser<AnnotationNameAndValue> PARSER = bi -> new AnnotationNameAndValue(bi.nextShort(), ElementValue.PARSER.parse(bi));

    private final int nameIndex;

    private final ElementValue value;

    public AnnotationNameAndValue(int nameIndex, ElementValue value) {
        this.nameIndex = nameIndex;
        this.value = value;
    }
}
