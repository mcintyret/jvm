package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class ClassCache {

    private static final Map<String, OopClass> MAP = new HashMap<>();

    public static OopClass getOopClass(ClassObject co) {
        OopClass oopClass = MAP.get(co.getType().getClassName());
        if (oopClass == null) {
            // shit

            ClassObject clazzObj = MagicClasses.getMagicClass(MagicClasses.JAVA_LANG_CLASS);
            OopClassClass instance = clazzObj.newObject((clazz, fields) -> new OopClassClass(clazz, null, fields, co));
            Heap.allocate(instance);
            oopClass = instance;
        }
        return oopClass;
    }

}
