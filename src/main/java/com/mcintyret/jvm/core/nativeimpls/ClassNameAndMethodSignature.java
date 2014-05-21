package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

/**
* User: tommcintyre
* Date: 5/21/14
*/
class ClassNameAndMethodSignature {

    private final String className;

    private final MethodSignature methodSignature;

    ClassNameAndMethodSignature(NativeImplementation nativeImplementation) {
        this(nativeImplementation.getClassName(), nativeImplementation.getMethodSignature());
    }

    ClassNameAndMethodSignature(String className, MethodSignature methodSignature) {
        this.className = className;
        this.methodSignature = methodSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassNameAndMethodSignature that = (ClassNameAndMethodSignature) o;

        if (!className.equals(that.className)) return false;
        if (!methodSignature.equals(that.methodSignature)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + methodSignature.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return className + "." + methodSignature;
    }
}
