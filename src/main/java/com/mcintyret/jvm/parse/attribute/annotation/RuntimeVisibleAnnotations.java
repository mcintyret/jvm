package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.AttributeType;
import java.util.List;

public class RuntimeVisibleAnnotations extends Annotations {

    public RuntimeVisibleAnnotations(List<Annotation> annotations) {
        super(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS, annotations);
    }
}
