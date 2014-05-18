package com.mcintyret.jvm.core.constantpool;

public class NameAndType {

    private final String name;

    // Might want to develop this a bit more
    private final String type;

    public NameAndType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


}
