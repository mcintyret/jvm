package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class InnerClass extends Attribute {

    private final List<InnerClassDetails> icds;

    public InnerClass(int length, List<InnerClassDetails> icds) {
        super(length);
        this.icds = icds;
    }
}
