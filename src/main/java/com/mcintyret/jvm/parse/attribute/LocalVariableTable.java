package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class LocalVariableTable extends Attribute {

    private final List<LocalVariable> localVariables;

    protected LocalVariableTable(int length, List<LocalVariable> localVariables) {
        super(length);
        this.localVariables = localVariables;
    }
}
