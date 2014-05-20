package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.parse.attribute.Attribute;
import com.mcintyret.jvm.parse.attribute.Attributes;
import com.mcintyret.jvm.parse.attribute.Parser;

public class MemberInfoParser implements Parser<MemberInfo> {

    private final Parser<Attribute> attributeParser;

    public MemberInfoParser(Parser<Attribute> attributeParser) {
        this.attributeParser = attributeParser;
    }

    @Override
    public MemberInfo parse(ByteIterator bi) {
        return new MemberInfo(
            bi.nextShort(),
            bi.nextShort(),
            bi.nextShort(),
            new Attributes(attributeParser.parseMulti(bi))
        );
    }
}
