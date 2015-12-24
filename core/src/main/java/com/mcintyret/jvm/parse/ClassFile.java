package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.parse.attribute.Attributes;
import java.util.List;
import java.util.Set;

public class ClassFile {

    private int minorVersion;

    private int majorVersion;

    private Object[] constantPool;

    private Set<Modifier> modifiers;

    private int thisClass;

    private int superClass;

    private int[] interfaces;

    private List<MemberInfo> fields;

    private List<MemberInfo> methods;

    private Attributes attributes;

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Object[] getConstantPool() {
        return constantPool;
    }

    public void setConstantPool(Object[] constantPool) {
        this.constantPool = constantPool;
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public void setAccessFlags(int accessFlags) {
        this.modifiers = Modifier.translate(accessFlags);
    }

    public int getThisClass() {
        return thisClass;
    }

    public void setThisClass(int thisClass) {
        this.thisClass = thisClass;
    }

    public int getSuperClass() {
        return superClass;
    }

    public void setSuperClass(int superClass) {
        this.superClass = superClass;
    }

    public int[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(int[] interfaces) {
        this.interfaces = interfaces;
    }

    public List<MemberInfo> getFields() {
        return fields;
    }

    public void setFields(List<MemberInfo> fields) {
        this.fields = fields;
    }

    public List<MemberInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MemberInfo> methods) {
        this.methods = methods;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }
}
