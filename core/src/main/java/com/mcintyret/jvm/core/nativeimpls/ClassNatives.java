package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.ImportantClasses;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopClassMethod;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.type.ReferenceType;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public enum ClassNatives implements NativeImplementation {
    REGISTER_NATIVES("registerNatives", "()V") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    DESIRED_ASSERTION_STATUS_0("desiredAssertionStatus0", "(Ljava/lang/Class;)Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forInt(0); // false
        }
    },
    GET_COMPONENT_TYPE("getComponentType", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);
            if (thisType instanceof ArrayType) {
                return NativeReturn.forReference(((ArrayType) thisType).getComponentType().getOopClassClass());
            } else {
                return NativeReturn.forNull();
            }
        }
    },
    GET_CLASSLOADER_0("getClassLoader0", "()Ljava/lang/ClassLoader;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            return NativeReturn.forNull();
        }
    },
    GET_PRIMITIVE_CLASS("getPrimitiveClass", "(Ljava/lang/String;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopClass stringObj = args.getOop(0);
            String arg = Utils.toString(stringObj);
            SimpleType st = SimpleType.valueOf(arg.toUpperCase());
            return NativeReturn.forReference(st.getOopClassClass());
        }
    },
    GET_DECLARED_FIELDS_0("getDeclaredFields0", "(Z)[Ljava/lang/reflect/Field;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);
            ClassObject fieldClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Field");

            ArrayClassObject fieldArrayType = ArrayClassObject.forType(ArrayType.create(fieldClass.getType(), 1));

            if (thisType.isPrimitive() || thisType.isArray()) {
                return NativeReturn.forReference(fieldArrayType.newArray(0));
            }


            ClassObject thisClassObject = ((NonArrayType) thisType).getClassObject();
            boolean publicOnly = args.getBoolean(1);
            List<Field> list = new ArrayList<>();

            addFields(thisClassObject.getStaticFields(), publicOnly, list);
            addFields(thisClassObject.getInstanceFields(), publicOnly, list);

            OopArray array = fieldArrayType.newArray(list.size());


            Method ctor = fieldClass.findConstructor("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;IILjava/lang/String;[B)V");
            for (int i = 0; i < list.size(); i++) {
                Field field = list.get(i);
                Variables ctorArgs = ctor.newArgArray();

                OopClass fieldObj = fieldClass.newObject();
                array.getFields().put(i, SimpleType.REF, Heap.allocate(fieldObj));

                ctorArgs.putOop(0, fieldObj);
                ctorArgs.putOop(1, thisType.getOopClassClass());
                ctorArgs.put(2, SimpleType.REF, Heap.intern(field.getName()));
                ctorArgs.putOop(3, field.getType().getOopClassClass());
                ctorArgs.putInt(4, Modifier.translate(field.getModifiers()));
                ctorArgs.putInt(5, i); // slot, this is my best guess as to what this means...
                ctorArgs.putNull(6);
                ctorArgs.putNull(7);

                Utils.executeMethodAndThrow(ctor, ctorArgs, ctx.getThread());
            }

            return NativeReturn.forReference(array);
        }

        private void addFields(Field[] fields, boolean publicOnly, List<Field> list) {
            for (Field field : fields) {
                if (!publicOnly || field.hasModifier(Modifier.PUBLIC)) {
                    list.add(field);
                }
            }
        }
    },
    GET_NAME_0("getName0", "()Ljava/lang/String;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);

            return NativeReturn.forInt(Heap.intern(thisType.toString()));
        }
    },
    FOR_NAME_0("forName0", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            String className = Utils.toString(args.<OopClass>getOop(0));
            className = className.replaceAll("\\.", "/");

            try {
                return NativeReturn.forReference(ClassLoader.getDefaultClassLoader().getClassObject(className).getOop());
            } catch (AssertionError e) {
                // TODO: proper error, or some other system!
                return NativeReturn.forThrowable(ClassLoader.getDefaultClassLoader().getClassObject("java/lang/ClassNotFoundException").newObject());
            }
        }
    },
    IS_INTERFACE("isInterface", "()Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);

            return NativeReturn.forBool(thisType.isInterface());
        }
    },
    IS_ASSIGNABLE_FROM("isAssignableFrom", "(Ljava/lang/Class;)Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            OopClassClass thisClass = args.getOop(0);
            OopClassClass thatClass = args.getOop(1);

            if (thisClass == thatClass) {
                return NativeReturn.forBool(true); // This takes care of all primitive classes
            }

            Type thisType = thisClass.getThisType();
            Type thatType = thatClass.getThisType();

            if (thisType.isPrimitive() || thatType.isPrimitive()) {
                return NativeReturn.forBool(false);
            }

            boolean instanceOf = getTypeClassObject(thisType).isInstanceOf(getTypeClassObject(thatType));
            return NativeReturn.forBool(instanceOf);
        }

        private AbstractClassObject getTypeClassObject(Type type) {
            return ((ReferenceType) type).getClassObject();
        }
    },
    GET_DECLARED_CONSTRUCTORS_0("getDeclaredConstructors0", "(Z)[Ljava/lang/reflect/Constructor;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // TODO: good god caching!
            OopClassClass clazz = args.getOop(0);
            Type thisType = clazz.getThisType();

            ClassObject ctorClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Constructor");
            ArrayClassObject ctorArrayClass = ArrayClassObject.forType(ArrayType.create(ctorClass.getType(), 1));

            if (thisType.isPrimitive() || thisType.isArray() || thisType.isInterface()) {
                OopArray ctors = ctorArrayClass.newArray(0); // TODO: cache this?
                return NativeReturn.forReference(ctors);
            } else {
                ClassObject thisClassObj = ((NonArrayType) thisType).getClassObject();

                boolean publicOnly = args.getBoolean(1);

                List<Method> ctors = new ArrayList<>();

                for (Method ctor : thisClassObj.getConstructors()) {
                    if (!publicOnly || ctor.hasModifier(Modifier.PUBLIC)) {
                        ctors.add(ctor);
                    }
                }

                Method ctorCtor = null;
                for (Method method : ctorClass.getConstructors()) {
                    if (method.getSignature().getArgTypes().size() > 0) {
                        ctorCtor = method;
                        break;
                    }
                }

                ArrayClassObject classArray = ArrayClassObject.forType(ArrayType.create(clazz.getClassObject().getType(), 1));

                OopArray result = ctorArrayClass.newArray(ctors.size());
                for (int i = 0; i < ctors.size(); i++) {
                    Method ctor = ctors.get(i);
                    OopClass ctorObj = ctorClass.newObject((thisClazz, fields) -> new OopClassMethod(thisClazz, fields, ctor));
                    Variables ctorArgs = ctorCtor.newArgArray();

                    ctorArgs.put(0, SimpleType.REF, Heap.allocate(ctorObj));
                    ctorArgs.putOop(1, clazz); // declaring class

                    OopArray paramTypes = classArray.newArray(ctor.getSignature().getArgTypes().size());
                    int j = 0;
                    for (Type type : ctor.getSignature().getArgTypes()) {
                        paramTypes.getFields().putOop(j++, type.getOopClassClass());
                    }

                    ctorArgs.put(2, SimpleType.REF, Heap.allocate(paramTypes));
                    // TODO
                    ctorArgs.putNull(3); // checkedExceptions
                    ctorArgs.putInt(4, Modifier.translate(ctor.getModifiers()));
                    ctorArgs.putInt(5, i); //slot - ??
                    ctorArgs.put(6, SimpleType.REF, Heap.intern(ctor.getSignature().toString()));
                    ctorArgs.putNull(7); // annotations
                    ctorArgs.putNull(8); // parameter annotations

                    Utils.executeMethodAndThrow(ctorCtor, ctorArgs, ctx.getThread());

                    result.getFields().putOop(i, ctorObj);
                }

                return NativeReturn.forReference(result);
            }
        }
    },
    GET_MODIFIERS("getModifiers", "()I") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            // TODO: cache?
            return NativeReturn.forInt(Modifier.translate(args.<OopClassClass>getOop(0).getThisType().getOopClassClass().getClassObject().getModifiers()));
        }
    },
    GET_SUPERCLASS("getSuperclass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);
            if (thisType.isPrimitive()) {
                return NativeReturn.forNull();
            } else {
                AbstractClassObject aco = ((ReferenceType) thisType).getClassObject();
                if (aco.hasModifier(Modifier.INTERFACE)) {
                    return NativeReturn.forNull();
                } else {
                    ClassObject superclass = aco.getSuperClass();
                    if (superclass == null) {
                        return NativeReturn.forNull(); // if thisType is java.lang.Object
                    } else {
                        return NativeReturn.forReference(superclass.getOop());
                    }
                }
            }
        }
    },
    IS_ARRAY("isArray", "()Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);
            return NativeReturn.forBool(thisType.isArray());
        }
    },
    IS_PRIMITIVE("isPrimitive", "()Z") {
        @Override
        public NativeReturn execute(Variables args, OperationContext ctx) {
            Type thisType = getThisType(args);
            return NativeReturn.forBool(thisType.isPrimitive());
        }
    };

    private static Type getThisType(Variables args) {
        OopClassClass thisClass = args.getOop(0);
        return thisClass.getThisType();
    }

    private final MethodSignature methodSignature;

    private ClassNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return ImportantClasses.JAVA_LANG_CLASS;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
