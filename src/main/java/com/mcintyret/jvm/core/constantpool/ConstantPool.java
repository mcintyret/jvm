package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ClassCache;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.cp.CpClass;
import com.mcintyret.jvm.parse.cp.CpFieldReference;
import com.mcintyret.jvm.parse.cp.CpMethodReference;
import com.mcintyret.jvm.parse.cp.CpString;

public class ConstantPool {

    private final Object[] constantPool;

    private final ClassLoader loader;

    public ConstantPool(Object[] constantPool, com.mcintyret.jvm.load.ClassLoader loader) {
        this.constantPool = constantPool;
        this.loader = loader;
    }

    public ClassObject getClassObject(int i) {
        if (constantPool[i] instanceof ClassObject) {
            return (ClassObject) constantPool[i];
        }

        return translactClassObject(i);
    }

    private ClassObject translactClassObject(int i) {
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

    public int getSingleWidth(int i) {
        if (constantPool[i] instanceof CpString) {
            int index = ((CpString) constantPool[i]).getStringIndex();
            String string = (String) constantPool[index];
            constantPool[i] = Heap.intern(string);
        } else {
            ClassObject co = null;
            // Asking for a class literal - this takes a bit of fiddling
            if (constantPool[i] instanceof CpClass) {
                co = translactClassObject(i);
            } else if (constantPool[i] instanceof ClassObject) {
                co = (ClassObject) constantPool[i];
            }
            if (co != null) {
                OopClass oop = ClassCache.getOopClass((ClassObject) constantPool[i]);
                return oop.getAddress();
            }

        }
        return (int) constantPool[i];
    }

    public long getDoubleWidth(int i) {
        return (long) constantPool[i];
    }

}
