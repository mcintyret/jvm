package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.NonArrayType;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.oop.OopClassMethod;
import com.mcintyret.jvm.core.opcode.OperationContext;
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
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forVoid();
        }
    },
    DESIRED_ASSERTION_STATUS_0("desiredAssertionStatus0", "(Ljava/lang/Class;)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forInt(0); // false
        }
    },
    GET_COMPONENT_TYPE("getComponentType", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            if (thisType instanceof ArrayType) {
                return NativeReturn.forReference(((ArrayType) thisType).getComponentType().getOopClassClass());
            } else {
                return NativeReturn.forNull();
            }
        }
    },
    GET_CLASSLOADER_0("getClassLoader0", "()Ljava/lang/ClassLoader;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            return NativeReturn.forNull();
        }
    },
    GET_PRIMITIVE_CLASS("getPrimitiveClass", "(Ljava/lang/String;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClass stringObj = Heap.getOopClass(args[0]);
            String arg = Utils.toString(stringObj);
            SimpleType st = SimpleType.valueOf(arg.toUpperCase());
            return NativeReturn.forReference(st.getOopClassClass());
        }
    },
    GET_DECLARED_FIELDS_0("getDeclaredFields0", "(Z)[Ljava/lang/reflect/Field;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            ClassObject fieldClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Field");

            ArrayClassObject fieldArrayType = ArrayClassObject.forType(ArrayType.create(fieldClass.getType(), 1));

            if (thisType.isPrimitive() || thisType.isArray()) {
                return NativeReturn.forReference(fieldArrayType.newArray(0));
            }


            ClassObject thisClassObject = ((NonArrayType) thisType).getClassObject();
            boolean publicOnly = args[1] != 0;
            List<Field> list = new ArrayList<>();

            addFields(thisClassObject.getStaticFields(), publicOnly, list);
            addFields(thisClassObject.getInstanceFields(), publicOnly, list);

            OopArray array = fieldArrayType.newArray(list.size());


            Method ctor = fieldClass.findConstructor("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;IILjava/lang/String;[B)V");
            for (int i = 0; i < list.size(); i++) {
                Field field = list.get(i);
                int[] ctorArgs = ctor.newArgArray();

                OopClass fieldObj = fieldClass.newObject();
                array.getFields()[i] = Heap.allocate(fieldObj);

                ctorArgs[0] = fieldObj.getAddress();
                ctorArgs[1] = thisType.getOopClassClass().getAddress();
                ctorArgs[2] = Heap.intern(field.getName());
                ctorArgs[3] = field.getType().getOopClassClass().getAddress();
                ctorArgs[4] = Modifier.translate(field.getModifiers());
                ctorArgs[5] = i; // slot, this is my best guess as to what this means...
                ctorArgs[6] = Heap.NULL_POINTER;
                ctorArgs[7] = Heap.NULL_POINTER;

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
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();

            return NativeReturn.forInt(Heap.intern(thisType.toString()));
        }
    },
    FOR_NAME_0("forName0", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            String className = Utils.toString(Heap.getOopClass(args[0]));
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
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();

            return NativeReturn.forBool(thisType.isInterface());
        }
    },
    IS_ASSIGNABLE_FROM("isAssignableFrom", "(Ljava/lang/Class;)Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClassClass thisClass = (OopClassClass) Heap.getOop(args[0]);
            OopClassClass thatClass = (OopClassClass) Heap.getOop(args[1]);

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
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // TODO: good god caching!
            OopClassClass clazz = (OopClassClass) Heap.getOop(args[0]);
            Type thisType = clazz.getThisType();

            ClassObject ctorClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Constructor");
            ArrayClassObject ctorArrayClass = ArrayClassObject.forType(ArrayType.create(ctorClass.getType(), 1));

            if (thisType.isPrimitive() || thisType.isArray() || thisType.isInterface()) {
                OopArray ctors = ctorArrayClass.newArray(0); // TODO: cache this?
                return NativeReturn.forReference(ctors);
            } else {
                ClassObject thisClassObj = ((NonArrayType) thisType).getClassObject();

                boolean publicOnly = args[1] > 0;

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
                    int[] ctorArgs = ctorCtor.newArgArray();

                    ctorArgs[0] = Heap.allocate(ctorObj);
                    ctorArgs[1] = clazz.getAddress(); // declaring class

                    OopArray paramTypes = classArray.newArray(ctor.getSignature().getArgTypes().size());
                    int j = 0;
                    for (Type type : ctor.getSignature().getArgTypes()) {
                        paramTypes.getFields()[j++] = type.getOopClassClass().getAddress();
                    }

                    ctorArgs[2] = Heap.allocate(paramTypes);
                    // TODO
                    ctorArgs[3] = Heap.NULL_POINTER; // checkedExceptions
                    ctorArgs[4] = Modifier.translate(ctor.getModifiers());
                    ctorArgs[5] = i; //slot - ??
                    ctorArgs[6] = Heap.intern(ctor.getSignature().toString());
                    ctorArgs[7] = Heap.NULL_POINTER; // annotations
                    ctorArgs[8] = Heap.NULL_POINTER; // parameter annotations

                    Utils.executeMethodAndThrow(ctorCtor, ctorArgs, ctx.getThread());

                    result.getFields()[i] = ctorObj.getAddress();
                }

                return NativeReturn.forReference(result);
            }
        }
    },
    GET_MODIFIERS("getModifiers", "()I") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            // TODO: cache?
            return NativeReturn.forInt(Modifier.translate(((OopClassClass) Heap.getOop(args[0])).getThisType().getOopClassClass().getClassObject().getModifiers()));
        }
    },
    GET_SUPERCLASS("getSuperclass", "()Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
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
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            return NativeReturn.forBool(thisType.isArray());
        }
    },
    IS_PRIMITIVE("isPrimitive", "()Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            Type thisType = ((OopClassClass) Heap.getOop(args[0])).getThisType();
            return NativeReturn.forBool(thisType.isPrimitive());
        }
    };

    private final MethodSignature methodSignature;

    private ClassNatives(String name, String descriptor) {
        methodSignature = MethodSignature.parse(name, descriptor);
    }


    @Override
    public String getClassName() {
        return MagicClasses.JAVA_LANG_CLASS;
    }

    @Override
    public MethodSignature getMethodSignature() {
        return methodSignature;
    }
}
