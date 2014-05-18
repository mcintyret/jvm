package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.Attribute;
import com.mcintyret.jvm.parse.attribute.AttributeType;
import java.util.List;

public abstract class Annotations extends Attribute {

    private final List<Annotation> annotations;

    public Annotations(AttributeType type, List<Annotation> annotations) {
        super(type);
        this.annotations = annotations;
    }
}
