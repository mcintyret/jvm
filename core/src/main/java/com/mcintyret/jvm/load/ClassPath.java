package com.mcintyret.jvm.load;

public interface ClassPath {

    public static ClassPath emptyClasspath() {
        return name -> null;
    }

    /**
     * Gets a ClassFileResource
     *
     * Returns null if the resource with the given name can't be found
     */
    ClassFileResource get(String name);

}
