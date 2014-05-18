package com.mcintyret.jvm.parse.attribute;

import com.mcintyret.jvm.core.ByteIterator;

public enum AttributeType {
    CONSTANT_VALUE("ConstantValue") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new ConstantValue(bi.nextInt(), bi.nextShort());
        }
    },
    CODE("Code") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Code(
                bi.nextInt(),
                bi.nextShort(),
                bi.nextShort(),
                bi.nextBytes(bi.nextInt()),
                Exception.PARSER.parseMulti(bi),
                attributeParser.parseMulti(bi)
            );
        }
    },
    EXCEPTIONS("Exceptions") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Exceptions(
                bi.nextInt(),
                Exception.PARSER.parseMulti(bi)
            );
        }
    },
    INNER_CLASSES("InnerClasses") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new InnerClass(
                bi.nextInt(),
                InnerClassDetails.PARSER.parseMulti(bi)
            );
        }
    },
    SYNTHETIC("Synthetic") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Synthetic(bi.nextInt());
        }
    },
    SOURCE_FILE("SourceFile") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new SourceFile(bi.nextInt(), bi.nextShort());
        }
    },
    LINE_NUMBER_TABLE("LineNumberTable") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new LineNumberTable(
                bi.nextInt(),
                LineNumber.PARSER.parseMulti(bi)
            );
        }
    },
    LOCAL_VARIABLE_TABLE("LocalVariableTable") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new LocalVariableTable(
                bi.nextInt(),
                LocalVariable.PARSER.parseMulti(bi)
            );
        }
    },
    DEPRECATED("Deprecated") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new Deprecated(bi.nextInt());
        }
    };

    private final String string;

    private AttributeType(String string) {
        this.string = string;
    }

    abstract Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser);

    public static AttributeType forString(String str) {
        for (AttributeType at : values()) {
            if (at.string.equals(str)) {
                return at;
            }
        }
        throw new IllegalArgumentException("No AttributeType named " + str);
    }
}
