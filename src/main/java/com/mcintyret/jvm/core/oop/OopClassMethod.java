package com.mcintyret.jvm.core.oop;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;

/**
 * User: tommcintyre
 * Date: 6/25/14
 */
public class OopClassMethod extends OopClass {

    private final Method method;

    public OopClassMethod(ClassObject classObject, int[] fields, Method method) {
        super(classObject, fields);
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
