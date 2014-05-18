package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class LocalVariableTable extends Attribute {

    private final List<LocalVariable> localVariables;

    protected LocalVariableTable(List<LocalVariable> localVariables) {
        super(AttributeType.LOCAL_VARIABLE_TABLE);
        this.localVariables = localVariables;
    }
}
