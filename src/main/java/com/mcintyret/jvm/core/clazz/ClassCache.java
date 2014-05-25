package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopPrimitiveClass;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class ClassCache {

    private static final Map<Type, OopClassClass> MAP = new HashMap<>();

    private static final Map<SimpleType, OopPrimitiveClass> PRIMITIVE = new EnumMap<>(SimpleType.class);

    public static OopClassClass getOopClass(AbstractClassObject co) {
        OopClassClass oopClass = MAP.get(co.getType());
        if (oopClass == null) {
            ClassObject clazzObj = MagicClasses.getMagicClass(MagicClasses.JAVA_LANG_CLASS);
            oopClass = clazzObj.newObject((clazz, fields) -> new OopClassClass(clazz, fields, co));
            Heap.allocate(oopClass);
        }
        return oopClass;
    }

    public static OopPrimitiveClass getOopPrimitive(SimpleType simpleType) {
        OopPrimitiveClass primitiveClass = PRIMITIVE.get(simpleType);
        if (primitiveClass == null) {
            ClassObject clazzObj = MagicClasses.getMagicClass(MagicClasses.JAVA_LANG_CLASS);
            primitiveClass = clazzObj.newObject((clazz, fields) -> new OopPrimitiveClass(clazz, fields, simpleType));
            Heap.allocate(primitiveClass);
        }
        return primitiveClass;
    }

}
