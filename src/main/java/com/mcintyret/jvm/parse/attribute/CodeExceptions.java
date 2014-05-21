package com.mcintyret.jvm.parse.attribute;

import java.util.List;

public class CodeExceptions extends Attribute {

    private final List<CodeException> codeExceptions;

    public CodeExceptions(List<CodeException> codeExceptions) {
        super(AttributeType.EXCEPTIONS);
        this.codeExceptions = codeExceptions;
    }
}
