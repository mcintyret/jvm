package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.Attribute;
import java.util.List;

public abstract class Annotations extends Attribute {

    private final List<Annotation> annotations;

    public Annotations(int length, List<Annotation> annotations) {
        super(length);
        this.annotations = annotations;
    }
}
