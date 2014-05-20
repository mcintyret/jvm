package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.load.Loader;
import com.mcintyret.jvm.parse.cp.CpClass;
import com.mcintyret.jvm.parse.cp.CpFieldReference;
import com.mcintyret.jvm.parse.cp.CpMethodReference;
import com.mcintyret.jvm.parse.cp.CpString;

public class ConstantPool {

    private final Object[] constantPool;

    private final Loader loader;

    public ConstantPool(Object[] constantPool, Loader loader) {
        this.constantPool = constantPool;
        this.loader = loader;
    }

    public ClassObject getClassObject(int i) {
        if (constantPool[i] instanceof ClassObject) {
            return (ClassObject) constantPool[i];
        }

        ClassObject obj = loader.translate((CpClass) constantPool[i], constantPool);
        constantPool[i] = obj;
        return obj;
    }

    public FieldReference getFieldReference(int i) {
        if (constantPool[i] instanceof FieldReference) {
            return (FieldReference) constantPool[i];
        }

        FieldReference ref = loader.translate((CpFieldReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    public MethodReference getMethodReference(int i) {
        if (constantPool[i] instanceof MethodReference) {
            return (MethodReference) constantPool[i];
        }

        MethodReference ref = loader.translate((CpMethodReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    // can return Long or Int
    // TODO: avoid autoboxing?
    public int getSingleWidth(int i) {
        if (constantPool[i] instanceof CpString) {
            int index = ((CpString) constantPool[i]).getStringIndex();
            String string = (String) constantPool[index];
            constantPool[i] = Heap.intern(string);
        }
        return (int) constantPool[i];
    }

    public long getDoubleWidth(int i) {
        return (long) constantPool[i];
    }

}
