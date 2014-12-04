package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.parse.Modifier;

import java.util.Set;

import static com.mcintyret.jvm.core.Utils.getClassObject;

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
    public boolean isInstanceOf(AbstractClassObject that) {
        if (that == this) {
            return true;
        }

        if (that == getClassObject(ImportantClasses.JAVA_LANG_OBJECT)) {
            return true; // everything extends object
        }

        boolean thatIsInterface = that.hasModifier(Modifier.INTERFACE);

        boolean thatIsArray = that.getType().isArray();

        if (this.getType().isArray() || thatIsArray) {
            if (this.getType().isArray() && thatIsArray) {
                return getComponentTypeClassObject(this).isInstanceOf(getComponentTypeClassObject(that));
            } else if (thatIsArray) {
                return false;
            }
        }


        if (hasModifier(Modifier.INTERFACE) && !thatIsInterface) {
            return false;
        }

        if (thatIsInterface) {
            for (ClassObject iface : interfaces) {
                if (iface.isInstanceOf(that)) {
                    return true;
                }
            }
        } else {
            ClassObject co = superClass;
            while (co != null) {
                if (co.isInstanceOf(that)) {
                    return true;
                }
                co = co.superClass;
            }
        }
        return false;
    }

    // TODO: this is too difficult!
    private static AbstractClassObject getComponentTypeClassObject(AbstractClassObject aco) {
        return ((ReferenceType) ((ArrayClassObject) aco).getType().getComponentType()).getClassObject();
    }

    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
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

    public OopClassClass getOop() {
        return getType().getOopClassClass();
    }
}
