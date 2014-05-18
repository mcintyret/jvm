package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.AttributeType;
import java.util.List;

public class RuntimeInvisibleAnnotations extends Annotations {

    public RuntimeInvisibleAnnotations(List<Annotation> annotations) {
        super(AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS, annotations);
    }
}
