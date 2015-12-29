package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.Variables;

import java.util.zip.Inflater;

// TODO: this is horrible!!
public class OopClassInflater extends OopClass {

    private final Inflater inflater;

    public OopClassInflater(ClassObject classObject, Variables fields) {
        super(classObject, fields);
        inflater = new Inflater();
    }


    public Inflater getInflater() {
        return inflater;
    }
}
