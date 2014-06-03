package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.parse.Modifier;

public abstract class AbstractClassObject {

    public abstract ReferenceType getType();

    protected final ClassObject superClass;

    private final ClassObject[] interfaces;

    private final Method[] instanceMethods;

    private final Set<Modifier> modifiers;

    protected AbstractClassObject(ClassObject superClass, ClassObject[] interfaces, Method[] instanceMethods,
                                  Set<Modifier> modifiers) {
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.instanceMethods = instanceMethods;
        this.modifiers = modifiers;
    }

    // TODO: can this be made more efficient?
    public boolean isInstanceOf(AbstractClassObject type) {
        if (type == this) {
            return true;
        }
        boolean argIsInterface = type.hasAttribute(Modifier.INTERFACE);

        if (hasAttribute(Modifier.INTERFACE) && !argIsInterface) {
            return false;
        }

        if (argIsInterface) {
            for (ClassObject iface : interfaces) {
                if (iface.isInstanceOf(type)) {
                    return true;
                }
            }
        } else {
            ClassObject co = superClass;
            while (co != null) {
                if (co.isInstanceOf(type)) {
                    return true;
                }
                co = co.superClass;
            }
        }
        return false;
    }

    public boolean hasAttribute(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public ClassObject getSuperClass() {
        return superClass;
    }

    public ClassObject[] getInterfaces() {
        return interfaces;
    }

    public Method[] getInstanceMethods() {
        return instanceMethods;
    }
}
