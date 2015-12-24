package com.mcintyret.jvm.parse.attribute;

public class LocalVariable {

    public static final Parser<LocalVariable> PARSER = bi -> new LocalVariable(bi.nextShort(), bi.nextShort(), bi.nextShort(), bi.nextShort(), bi.nextShort());

    private final int startPc;

    private final int length;

    private final int nameIndex;

    private final int descriptorIndex;

    private final int index;

    public LocalVariable(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
        this.startPc = startPc;
        this.length = length;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.index = index;
    }
}
