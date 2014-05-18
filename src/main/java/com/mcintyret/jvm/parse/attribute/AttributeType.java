package com.mcintyret.jvm.parse.attribute;

import com.mcintyret.jvm.core.ByteIterator;
import com.mcintyret.jvm.parse.attribute.annotation.Annotation;
import com.mcintyret.jvm.parse.attribute.annotation.RuntimeInvisibleAnnotations;
import com.mcintyret.jvm.parse.attribute.annotation.RuntimeVisibleAnnotations;

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
    ENCLOSING_METHOD("EnclosingMethod") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new EnclosingMethod(bi.nextInt(), bi.nextShort(), bi.nextShort());
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
    },
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new RuntimeVisibleAnnotations(bi.nextInt(), Annotation.PARSER.parseMulti(bi));
        }
    },
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations") {
        @Override
        Attribute parse(ByteIterator bi, Parser<Attribute> attributeParser) {
            return new RuntimeInvisibleAnnotations(bi.nextInt(), Annotation.PARSER.parseMulti(bi));
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
