package com.mcintyret.jvm.load;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.Iterator;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class FilteringClassPath implements ClassPath {

    private final ClassPath classPath;

    private final Predicate<ClassFileResource> predicate;

    public FilteringClassPath(ClassPath classPath, Predicate<ClassFileResource> predicate) {
        this.classPath = classPath;
        this.predicate = predicate;
    }

    @Override
    public Iterator<ClassFileResource> iterator() {
        return Iterators.filter(classPath.iterator(), predicate);
    }
}
