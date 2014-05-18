package com.mcintyret.jvm.parse.attribute.annotation;

import com.mcintyret.jvm.parse.attribute.Attribute;
import com.mcintyret.jvm.parse.attribute.AttributeType;
import java.util.EnumMap;
import java.util.Map;

public class Attributes {

    private final Map<AttributeType, Attribute> attributes = new EnumMap<>(AttributeType.class);

    public Attributes(Iterable<Attribute> attributes) {
        for (Attribute attribute : attributes) {
            this.attributes.put(attribute.getType(), attribute);
        }
    }

    public Attribute getAttribute(AttributeType type) {
        return attributes.get(type);
    }

}
