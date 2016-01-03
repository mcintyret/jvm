package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.exec.Variables;

import java.util.zip.Inflater;


/**
 * The java.util.zip.Inflater class has some native member methods that depend on the internal state of that Inflater.
 *
 * These are essential for reading zip files, including jar files, which comes up all the time.
 *
 * Therefore, for every Oop representing an Inflater we need a corresponding 'real' Inflater instance which will have
 * identical state and therefore return the correct result when our native calls delegate to it.
 *
 * TODO: is there a better way?
 */
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
