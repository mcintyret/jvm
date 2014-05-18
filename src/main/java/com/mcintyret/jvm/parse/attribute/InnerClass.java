package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class InnerClass extends Attribute {

    private final List<InnerClassDetails> icds;

    public InnerClass(List<InnerClassDetails> icds) {
        super(AttributeType.INNER_CLASSES);
        this.icds = icds;
    }
}
