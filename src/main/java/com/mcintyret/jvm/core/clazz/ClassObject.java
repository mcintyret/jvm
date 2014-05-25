package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public class ClassObject extends AbstractClassObject {

    private final ReferenceType type;

    private final ConstantPool constantPool;

    private final Method[] instanceMethods;

    private final Method[] staticMethods;

    private final Field[] instanceFields;

    private final Field[] staticFields;

    private final int[] staticFieldValues;

    private final ClassLoader classLoader;

    public ClassObject(ReferenceType type, Set<Modifier> modifiers, ClassObject parent, ClassObject[] interfaces,
                       ConstantPool constantPool, Method[] instanceMethods, Method[] staticMethods,
                       Field[] instanceFields, Field[] staticFields, ClassLoader classLoader) {
        super(parent, interfaces, modifiers);
        this.type = type;
        this.constantPool = constantPool;
        this.instanceMethods = instanceMethods;
        this.staticMethods = staticMethods;
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.staticFieldValues = newInstanceFieldsValuesArray(staticFields);
        this.classLoader = classLoader;

        finalizeMembers(instanceFields);
        finalizeMembers(instanceMethods);
        finalizeMembers(staticFields);
        finalizeMembers(staticMethods);
    }

    public Method[] getInstanceMethods() {
        return instanceMethods;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public Field[] getInstanceFields() {
        return instanceFields;
    }

    public int[] getStaticFieldValues() {
        return staticFieldValues;
    }

    public Field[] getStaticFields() {
        return staticFields;
    }

    public OopClass newObject() {
        return newObject(DefaultNewObjectCreator.INSTANCE);
    }

    <O extends OopClass> O newObject(NewObjectCreator<O> objectCreator) {
        return objectCreator.newObject(this, newInstanceFieldsValuesArray(instanceFields));
    }

    private static int[] newInstanceFieldsValuesArray(Field[] fields) {
        if (fields.length == 0) {
            return new int[0];
        }
        Field lastField = fields[fields.length - 1];
        int size = lastField.getOffset() + lastField.getType().getSimpleType().getWidth();
        return new int[size];
    }

    public Method findMethod(String name, String descriptor, boolean isStatic) {
        return findMethod(MethodSignature.parse(name, descriptor), isStatic);
    }

    public Method findMethod(MethodSignature methodSignature, boolean isStatic) {
        return isStatic ? findMethod(methodSignature, staticMethods) : findMethod(methodSignature, instanceMethods);
    }

    private static Method findMethod(MethodSignature methodSignature, Method[] methods) {
        for (Method method : methods) {
            if (methodSignature.equals(method.getSignature())) {
                return method;
            }
        }
        return null;
    }

    @Override
    public ReferenceType getType() {
        return type;
    }

    public String getClassName() {
        return type.getClassName();
    }

    public Method[] getStaticMethods() {
        return staticMethods;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }



    private void finalizeMembers(Member[] members) {
        for (Member member : members)  {
            if (member.getClassObject() == null) {
                // So we can reuse the same object for overridden methods
                member.setClassObject(this);
            }
        }
    }

    interface NewObjectCreator<O extends OopClass> {

        O newObject(ClassObject clazz, int[] fields);

    }

    @Override
    public String toString() {
        return "Class[" + getClassName() + "]";
    }

    private enum DefaultNewObjectCreator implements NewObjectCreator<OopClass> {
        INSTANCE;

        @Override
        public OopClass newObject(ClassObject clazz, int[] fields) {
            return new OopClass(clazz, fields);
        }
    }

}
