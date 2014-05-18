package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.Parser;
import java.util.List;

public class Annotation {

    public static final Parser<Annotation> PARSER = bi -> new Annotation(bi.nextShort(), AnnotationNameAndValue.PARSER.parseMulti(bi));

    private final int typeIndex;

    private final List<AnnotationNameAndValue> nameAndValues;

    public Annotation(int typeIndex, List<AnnotationNameAndValue> nameAndValues) {
        this.typeIndex = typeIndex;
        this.nameAndValues = nameAndValues;
    }
}
