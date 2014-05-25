package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.parse.Modifier;
import java.util.Set;

public abstract class AbstractClassObject {

    public abstract Type getType();

    protected final ClassObject superClass;

    private final ClassObject[] interfaces;

    private final Set<Modifier> modifiers;

    protected AbstractClassObject(ClassObject superClass, ClassObject[] interfaces, Set<Modifier> modifiers) {
        this.superClass = superClass;
        this.interfaces = interfaces;
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
}
