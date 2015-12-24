package com.mcintyret.jvm.parse.attribute.annotation;

import java.util.List;

import com.mcintyret.jvm.parse.attribute.AttributeType;

public class RuntimeInvisibleAnnotations extends Annotations {

    public RuntimeInvisibleAnnotations(List<Annotation> annotations) {
        super(AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS, annotations);
    }
}
