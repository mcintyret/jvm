package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.parse.Modifier;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

public class ArrayClassObject extends AbstractClassObject {

    private static final ClassObject PARENT = getDefaultClassLoader().getClassObject(ImportantClasses.JAVA_LANG_OBJECT);

    private static final ClassObject[] INTERFACES = {
            getDefaultClassLoader().getClassObject(ImportantClasses.JAVA_LANG_CLONEABLE),
            getDefaultClassLoader().getClassObject(ImportantClasses.JAVA_IO_SERIALIZABLE)
    };

    private static final Set<Modifier> MODIFIERS = EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.ABSTRACT);

    private static final Map<ArrayType, ArrayClassObject> CACHE = new HashMap<>();

    public static ArrayClassObject forType(ArrayType type) {
        ArrayClassObject aco = CACHE.get(type);
        if (aco == null) {
            aco = new ArrayClassObject(type);
            CACHE.put(type, aco);
        }
        return aco;
    }

    private final ArrayType arrayType;

    private ArrayClassObject(ArrayType arrayType) {
        super(PARENT, INTERFACES, PARENT.getInstanceMethods(), MODIFIERS);
        this.arrayType = arrayType;
    }

    public OopArray newArray(int length) {
        length *= arrayType.getComponentType().getWidth();
        return new OopArray(this, new int[length]);
    }

    @Override
    public ArrayType getType() {
        return arrayType;
    }

}
