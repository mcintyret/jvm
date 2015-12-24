package com.mcintyret.jvm.load;

import com.google.common.collect.Iterators;

import java.util.Iterator;

public interface ClassPath extends Iterable<ClassFileResource> {

    public static ClassPath emptyClasspath() {
        return Iterators::emptyIterator;
    }

    default Iterator<ClassFileResource> classFileFilteringIterator(Iterator<ClassFileResource> it) {
        return Iterators.filter(it, classFileResource -> classFileResource.getName().endsWith(".class"));
    }

}
