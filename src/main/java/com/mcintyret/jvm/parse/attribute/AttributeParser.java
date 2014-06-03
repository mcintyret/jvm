package com.mcintyret.jvm.parse.attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcintyret.jvm.core.ByteIterator;

public class AttributeParser implements Parser<Attribute> {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeParser.class);

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
            LOG.error("Looking for String at index {} but found {}", index, constantPool[index]);
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
