package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.parse.attribute.Parser;

public abstract class ElementValue {

    public static final Parser<ElementValue> PARSER = new Parser<ElementValue>() {
        @Override
        public ElementValue parse(ByteIterator bi) {
            char c = (char) bi.next();
            switch (c) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 's':
                    return new ConstantElementValue(c, bi.nextShort());
                case 'c':
                    return new ClassElementValue(c, bi.nextShort());
                case 'e':
                    return new EnumElementValue(c, bi.nextShort(), bi.nextShort());
                case '@':
                    return new AnnotationElementValue(c, Annotation.PARSER.parse(bi));
                case '[':
                    return new ArrayElementValue(c, parseMulti(bi));
                default:
                    throw new IllegalArgumentException("Unknown annotation element value type: " + c);
            }
        }
    };


    private final char tag;

    protected ElementValue(char tag) {
        this.tag = tag;
    }
}
