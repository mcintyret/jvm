package com.mcintyret.jvm.parse.attribute.annotation;

public class AnnotationElementValue extends ElementValue {

    private final Annotation annotation;

    protected AnnotationElementValue(char tag, Annotation annotation) {
        super(tag);
        this.annotation = annotation;
    }
}
