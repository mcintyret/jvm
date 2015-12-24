package com.mcintyret.jvm.parse;

import com.google.common.io.ByteStreams;
import com.mcintyret.jvm.core.util.ByteBufferIterator;
import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.parse.attribute.Attribute;
import com.mcintyret.jvm.parse.attribute.AttributeParser;
import com.mcintyret.jvm.parse.attribute.Attributes;
import com.mcintyret.jvm.parse.attribute.Parser;
import com.mcintyret.jvm.parse.cp.ConstantPoolConstant;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassFileReader {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public ClassFile read(InputStream is) throws IOException {
        ByteIterator bi = new ByteBufferIterator(ByteStreams.toByteArray(is));
        verifyMagicNumber(bi);

        ClassFile classFile = new ClassFile();

        classFile.setMinorVersion(bi.nextShort());
        classFile.setMajorVersion(bi.nextShort());

        int cpCount = bi.nextShort();
        Object[] cp = new Object[cpCount];
        for (int i = 1; i < cpCount; i++) {
            ConstantPoolConstant cpc = ConstantPoolConstant.forByte(bi.nextByte());
            cp[i] = cpc.parse(bi);
            if (cpc == ConstantPoolConstant.DOUBLE || cpc == ConstantPoolConstant.LONG) {
                i++;
            }
        }
        classFile.setConstantPool(cp);

        classFile.setAccessFlags(bi.nextShort());

        classFile.setThisClass(bi.nextShort());
        classFile.setSuperClass(bi.nextShort());

        int[] interfaces = new int[bi.nextShort()];
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = bi.nextShort();
        }
        classFile.setInterfaces(interfaces);

        Parser<Attribute> attributeParser = new AttributeParser(cp);
        Parser<MemberInfo> fieldOrMethodInfoParser =
            new MemberInfoParser(attributeParser);

        classFile.setFields(fieldOrMethodInfoParser.parseMulti(bi));
        classFile.setMethods(fieldOrMethodInfoParser.parseMulti(bi));
        classFile.setAttributes(new Attributes(attributeParser.parseMulti(bi)));

        return classFile;
    }

    private static void verifyMagicNumber(ByteIterator bi) {
        if (bi.nextInt() != MAGIC_NUMBER) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws IOException {
        String file = "/Users/mcintyret2/Github/jvm/target/classes/com/mcintyret/jvm/core/Accessor.class";
        ClassFile cf = new ClassFileReader().read(Files.newInputStream(Paths.get(file)));
        System.out.println(cf);
    }

}
