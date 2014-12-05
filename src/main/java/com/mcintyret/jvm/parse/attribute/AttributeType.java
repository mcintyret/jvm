package com.mcintyret.jvm.parse.attribute;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.parse.attribute.annotation.Annotation;
import com.mcintyret.jvm.parse.attribute.annotation.RuntimeInvisibleAnnotations;
import com.mcintyret.jvm.parse.attribute.annotation.RuntimeVisibleAnnotations;

public enum AttributeType {
    CONSTANT_VALUE("ConstantValue") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new ConstantValue(bi.nextShort());
        }
    },
    CODE("Code") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Code(
                bi.nextShort(),
                bi.nextShort(),
                bi.nextBytes(bi.nextInt()),
                CodeException.PARSER.parseMulti(bi),
                new Attributes(attributeParser.parseMulti(bi))
            );
        }
    },
    EXCEPTIONS("Exceptions") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Exceptions(
                Exception.PARSER.parseMulti(bi)
            );
        }
    },
    ENCLOSING_METHOD("EnclosingMethod") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new EnclosingMethod(bi.nextShort(), bi.nextShort());
        }
    },
    INNER_CLASSES("InnerClasses") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new InnerClass(
                InnerClassDetails.PARSER.parseMulti(bi)
            );
        }
    },
    SYNTHETIC("Synthetic") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Synthetic();
        }
    },
    SOURCE_FILE("SourceFile") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new SourceFile(bi.nextShort());
        }
    },
    LINE_NUMBER_TABLE("LineNumberTable") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new LineNumberTable(
                LineNumber.PARSER.parseMulti(bi)
            );
        }
    },
    LOCAL_VARIABLE_TABLE("LocalVariableTable") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new LocalVariableTable(
                LocalVariable.PARSER.parseMulti(bi)
            );
        }
    },
    DEPRECATED("Deprecated") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Deprecated();
        }
    },
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new RuntimeVisibleAnnotations(Annotation.PARSER.parseMulti(bi));
        }
    },
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new RuntimeInvisibleAnnotations(Annotation.PARSER.parseMulti(bi));
        }
    },
    SIGNATURE("Signature") {
        @Override
        Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Signature(bi.nextShort());
        }
    };

    private final String string;

    private AttributeType(String string) {
        this.string = string;
    }

    abstract Attribute doParse(ByteIterator bi, Parser<Attribute> attributeParser);

    Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
        bi.nextInt(); // The size, which we don't care about;
        return doParse(bi, attributeParser);
    }

    public static AttributeType forString(String str) {
        for (AttributeType at : values()) {
            if (at.string.equals(str)) {
                return at;
            }
        }
//        System.err.println("No AttributeType named " + str);
        return null;
    }
}
