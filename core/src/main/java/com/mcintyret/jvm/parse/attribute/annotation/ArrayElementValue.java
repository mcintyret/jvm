package com.mcintyret.jvm.parse.attribute.annotation;

import java.util.List;

public class ArrayElementValue extends ElementValue {

    private final List<ElementValue> values;

    protected ArrayElementValue(char tag, List<ElementValue> values) {
        super(tag);
        this.values = values;
    }
}
