package com.mcintyret.jvm.load;

import com.google.common.collect.Iterators;

import java.util.Collections;
import java.util.Iterator;

public interface ClassPath extends Iterable<ClassFileResource> {

    public static ClassPath emptyClasspath() {
        return new ClassPath() {
            @Override
            public ClassFileResource get(String name) {
                return null;
            }

            @Override
            public Iterator<ClassFileResource> iterator() {
                return Collections.emptyIterator();
            }
        };
    }

    default Iterator<ClassFileResource> classFileFilteringIterator(Iterator<ClassFileResource> it) {
        return Iterators.filter(it, classFileResource -> classFileResource.getName().endsWith(".class"));
    }

    ClassFileResource get(String name);

}
