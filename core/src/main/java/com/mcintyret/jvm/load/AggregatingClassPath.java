package com.mcintyret.jvm.load;

import static java.util.Arrays.asList;

import java.util.Iterator;

import com.google.common.collect.Iterables;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class AggregatingClassPath implements ClassPath {

    private final Iterable<ClassPath> classPaths;

    public AggregatingClassPath(Iterable<ClassPath> classPaths) {
        this.classPaths = classPaths;
    }

    public AggregatingClassPath(ClassPath... classPaths) {
        this(asList(classPaths));
    }

    @Override
    public Iterator<ClassFileResource> iterator() {
        return Iterables.concat(classPaths).iterator();
    }
}
