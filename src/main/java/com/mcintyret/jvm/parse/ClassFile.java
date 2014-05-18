package com.mcintyret.jvm.parse;

import com.mcintyret.jvm.parse.attribute.Attribute;
import java.util.List;

public class ClassFile {

    private int minorVersion;

    private int majorVersion;

    private Object[] constantPool;

    private int accessFlags;

    private int thisClass;

    private int superClass;

    private int[] interfaces;

    private List<FieldOrMethodInfo> fields;

    private List<FieldOrMethodInfo> methods;

    private List<Attribute> attributes;

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

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
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

    public List<FieldOrMethodInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldOrMethodInfo> fields) {
        this.fields = fields;
    }

    public List<FieldOrMethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<FieldOrMethodInfo> methods) {
        this.methods = methods;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
