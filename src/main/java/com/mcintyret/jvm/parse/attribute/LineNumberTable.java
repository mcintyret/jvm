package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class LineNumberTable extends Attribute {

    private final List<LineNumber> lineNumbers;

    protected LineNumberTable(List<LineNumber> lineNumbers) {
        super(AttributeType.LINE_NUMBER_TABLE);
        this.lineNumbers = lineNumbers;
    }
}
