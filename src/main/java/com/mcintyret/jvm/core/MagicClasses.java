package com.mcintyret.jvm.core;

import com.mcintyret.jvm.core.clazz.ClassObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mcintyret.jvm.core.Assert.assertNotNull;
import static java.util.Arrays.asList;

public class MagicClasses {

    public static final String JAVA_LANG_OBJECT = "java/lang/Object";
    public static final String JAVA_LANG_STRING = "java/lang/String";
    public static final String JAVA_LANG_CLONEABLE = "java/lang/Cloneable";
    public static final String JAVA_IO_SERIALIZABLE = "java/io/Serializable";

    private static final Set<String> MAGIC_CLASS_NAMES = Collections.unmodifiableSet(new HashSet<>(asList(
        JAVA_LANG_OBJECT,
        JAVA_LANG_STRING,
        JAVA_LANG_CLONEABLE,
        JAVA_IO_SERIALIZABLE
    )));

    private static final Map<String, ClassObject> MAGIC_CLASSES = new HashMap<>();

    public static boolean registerClass(ClassObject classObject) {
        String name = classObject.getType().getClassName();
        if (MAGIC_CLASS_NAMES.contains(name)) {
            MAGIC_CLASSES.put(name, classObject);
            return true;
        }
        return false;
    }

    public static ClassObject getMagicClass(String className) {
        return assertNotNull(MAGIC_CLASSES.get(className));
    }

}
