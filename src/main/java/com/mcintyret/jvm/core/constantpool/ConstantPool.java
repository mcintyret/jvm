package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ClassCache;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
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

    public AbstractClassObject getClassObject(int i) {
        if (constantPool[i] instanceof AbstractClassObject) {
            return (AbstractClassObject) constantPool[i];
        }

        return translateClassObject(i);
    }

    private AbstractClassObject translateClassObject(int i) {
        AbstractClassObject obj = loader.translate((CpClass) constantPool[i], constantPool);
        constantPool[i] = obj;
        return obj;
    }

    public Field getField(int i) {
        if (constantPool[i] instanceof Field) {
            return (Field) constantPool[i];
        }

        Field ref = loader.translate((CpFieldReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    public Method getMethod(int i) {
        if (constantPool[i] instanceof Method) {
            return (Method) constantPool[i];
        }

        Method ref = loader.translate((CpMethodReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    public int getSingleWidth(int i) {
        if (constantPool[i] instanceof CpString) {
            int index = ((CpString) constantPool[i]).getStringIndex();
            String string = (String) constantPool[index];
            constantPool[i] = Heap.intern(string);
        } else {
            AbstractClassObject co = null;
            // Asking for a class literal - this takes a bit of fiddling
            if (constantPool[i] instanceof CpClass) {
                co = translateClassObject(i);
            } else if (constantPool[i] instanceof AbstractClassObject) {
                co = (AbstractClassObject) constantPool[i];
            }
            if (co != null) {
                OopClass oop = ClassCache.getOopClass((AbstractClassObject) constantPool[i]);
                return oop.getAddress();
            }

        }
        return (int) constantPool[i];
    }

    public long getDoubleWidth(int i) {
        return (long) constantPool[i];
    }

}
