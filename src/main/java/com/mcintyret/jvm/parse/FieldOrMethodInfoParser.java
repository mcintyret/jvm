package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.parse.attribute.Attribute;
import com.mcintyret.jvm.parse.attribute.Parser;

public class FieldOrMethodInfoParser implements Parser<FieldOrMethodInfo> {

    private final Parser<Attribute> attributeParser;

    public FieldOrMethodInfoParser(Parser<Attribute> attributeParser) {
        this.attributeParser = attributeParser;
    }

    @Override
    public FieldOrMethodInfo parse(ByteIterator bi) {
        return new FieldOrMethodInfo(
            bi.nextShort(),
            bi.nextShort(),
            bi.nextShort(),
            attributeParser.parseMulti(bi)
        );
    }
}
