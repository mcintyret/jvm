package com.mcintyret.jvm.parse.attribute;

import java.util.EnumMap;
import java.util.Map;

public class Attributes {

    private final Map<AttributeType, Attribute> attributes = new EnumMap<>(AttributeType.class);

    public Attributes(Iterable<Attribute> attributes) {
        for (Attribute attribute : attributes) {
            if (attribute != null) {
                this.attributes.put(attribute.getType(), attribute);
            }
        }
    }

    public Attribute getAttribute(AttributeType type) {
        return attributes.get(type);
    }

}
