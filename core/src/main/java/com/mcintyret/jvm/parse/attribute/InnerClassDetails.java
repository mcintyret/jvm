package com.mcintyret.jvm.parse.attribute;

public class InnerClassDetails {

    public static final Parser<InnerClassDetails> PARSER = bi -> new InnerClassDetails(bi.nextShort(), bi.nextShort(), bi.nextShort(), bi.nextShort());

    private final int innerClassInfoIndex;

    private final int outerClassInfoIndex;

    private final int innerNameIndex;

    private final int innerClassAccessFlags;

    private InnerClassDetails(int innerClassInfoIndex, int outerClassInfoIndex, int innerNameIndex, int innerClassAccessFlags) {
        this.innerClassInfoIndex = innerClassInfoIndex;
        this.outerClassInfoIndex = outerClassInfoIndex;
        this.innerNameIndex = innerNameIndex;
        this.innerClassAccessFlags = innerClassAccessFlags;
    }
}
