package com.mcintyret.jvm.parse.attribute.annotation;

import java.util.List;

import com.mcintyret.jvm.parse.attribute.AttributeType;

public class RuntimeVisibleAnnotations extends Annotations {

    public RuntimeVisibleAnnotations(List<Annotation> annotations) {
        super(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS, annotations);
    }
}
