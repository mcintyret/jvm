package com.mcintyret.jvm.parse.attribute;

import com.mcintyret.jvm.core.ByteIterator;

public class AttributeParser implements Parser<Attribute> {

    private final Object[] constantPool;

    public AttributeParser(Object[] constantPool) {
        this.constantPool = constantPool;
    }

    @Override
    public Attribute parse(ByteIterator bi) {
        int index = bi.nextShort();
        String name;
        try {
            name = (String) constantPool[index];
        } catch (Throwable t) {
            System.out.println("Looking for String at index " + index + " but found " + constantPool[index]);
            throw t;
        }
        AttributeType at = AttributeType.forString(name);
        if (at == null) {
            // not found - need to skip the buffer!
            bi.nextBytes(bi.nextInt());
            return null;
        }
        return at.parse(bi, this);
    }
}
