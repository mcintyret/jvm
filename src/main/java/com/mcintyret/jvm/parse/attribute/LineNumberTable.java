package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class LineNumberTable extends Attribute {

    private final List<LineNumber> lineNumbers;

    protected LineNumberTable(int length, List<LineNumber> lineNumbers) {
        super(length);
        this.lineNumbers = lineNumbers;
    }
}
