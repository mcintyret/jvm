package com.mcintyret.jvm.parse.cp;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class CpInvokeDynamicInfo {

    private final int bootstrapMethodAttrIndex;

    private final int nameAndTypeIndex;

    public CpInvokeDynamicInfo(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}
