package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.NonArrayType;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;

import java.util.Set;

public class ClassObject extends AbstractClassObject {

    private final NonArrayType type;

    private final ConstantPool constantPool;

    private final Method[] constructors;

    private final Method[] staticMethods;

    private final Field[] instanceFields;

    private final Field[] staticFields;

    private final int[] staticFieldValues;

    private final ClassLoader classLoader;

    public ClassObject(NonArrayType type, Set<Modifier> modifiers, ClassObject parent, ClassObject[] interfaces,
                       ConstantPool constantPool, Method[] instanceMethods, Method[] constructors, Method[] staticMethods,
                       Field[] instanceFields, Field[] staticFields, ClassLoader classLoader) {
        super(parent, interfaces, instanceMethods, modifiers);
        this.type = type;
        this.constantPool = constantPool;
        this.constructors = constructors;
        this.staticMethods = staticMethods;
        this.instanceFields = instanceFields;
        this.staticFields = staticFields;
        this.staticFieldValues = newInstanceFieldsValuesArray(staticFields);
        this.classLoader = classLoader;

        finalizeMembers(instanceFields);
        finalizeMembers(instanceMethods);
        finalizeMembers(staticFields);
        finalizeMembers(staticMethods);
        finalizeMembers(constructors);
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
        return newObject(OopClass::new);
    }

    public <O extends OopClass> O newObject(NewObjectCreator<O> objectCreator) {
        return objectCreator.newObject(this, newInstanceFieldsValuesArray(instanceFields));
    }

    private static int[] newInstanceFieldsValuesArray(Field[] fields) {
        if (fields.length == 0) {
            return new int[0];
        }
        Field lastField = fields[fields.length - 1];
        int size = lastField.getOffset() + lastField.getType().getWidth();
        return new int[size];
    }

    public Method findMethod(String name, boolean isStatic) {
        return findMethod(name, isStatic ? staticMethods : getInstanceMethods());
    }

    private Method findMethod(String name, Method[] methods) {
        Method found = null;
        for (Method method : methods) {
            if (method.getSignature().getName().equals(name)) {
                if (found == null) {
                    found = method;
                } else {
                    throw new IllegalArgumentException("Multiple methods with name " + name);
                }
            }
        }

        if (found == null) {
            throw new IllegalArgumentException("No methods with name " + name);
        }
        return found;
    }

    public Method findMethod(String name, String descriptor, boolean isStatic) {
        return findMethod(MethodSignature.parse(name, descriptor), isStatic);
    }

    public Method findMethod(MethodSignature methodSignature, boolean isStatic) {
        return isStatic ? findMethod(methodSignature, staticMethods) : findMethod(methodSignature, getInstanceMethods());
    }

    public Method getDefaultConstructor() {
        return findConstructor("()V");
    }

    public Method findConstructor(String descriptor) {
        return findMethod(MethodSignature.parse(Method.CONSTRUCTOR_METHOD_NAME, descriptor), constructors);
    }

    public Field findField(String name, boolean isStatic) {
        Field[] fields = isStatic ? staticFields : instanceFields;
        for (Field field : fields) {
            if (name.equals(field.getName())) {
                return field;
            }
        }

        throw new IllegalArgumentException("No " + (isStatic ? "static " : "") + "field named '" + name + "' on class " + getClassName());
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
    public NonArrayType getType() {
        return type;
    }

    public String getClassName() {
        return type.getClassName();
    }

    public Method[] getStaticMethods() {
        return staticMethods;
    }

    public Method[] getConstructors() {
        return constructors;
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

    public interface NewObjectCreator<O extends OopClass> {

        O newObject(ClassObject clazz, int[] fields);

    }

    @Override
    public String toString() {
        return "Class[" + getClassName() + "]";
    }

}
