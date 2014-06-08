package com.mcintyret.jvm.core.nativeimpls;

import java.util.ArrayList;
import java.util.List;

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
import com.mcintyret.jvm.core.domain.SimpleType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.domain.Types;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.oop.OopClassClass;
import com.mcintyret.jvm.core.opcode.OperationContext;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.Modifier;

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
            return NativeReturn.forReference(st.getClassOop());
        }
    },
    GET_DECLARED_FIELDS_0("getDeclaredFields0", "(Z)[Ljava/lang/reflect/Field;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClassClass thisClass = (OopClassClass) Heap.getOopClass(args[0]);
            ClassObject thisClassObject = (ClassObject) thisClass.getThisClass();
            boolean publicOnly = args[1] != 0;
            List<Field> list = new ArrayList<>();

            addFields(thisClassObject.getStaticFields(), publicOnly, list);
            addFields(thisClassObject.getInstanceFields(), publicOnly, list);

            ArrayClassObject arrayType = ArrayClassObject.forType(ArrayType.create(Types.parseType("Ljava/lang/reflect/Field;"), 1));
            OopArray array = arrayType.newArray(list.size());

            ClassObject fieldClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Field");
            Method ctor = fieldClass.findConstructor("(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;IILjava/lang/String;[B)V");
            for (int i = 0; i < list.size(); i++) {
                Field field = list.get(i);
                int[] ctorArgs = ctor.newArgArray();

                OopClass fieldObj = fieldClass.newObject();
                array.getFields()[i] = Heap.allocate(fieldObj);

                ctorArgs[0] = fieldObj.getAddress();
                ctorArgs[1] = thisClass.getAddress();
                ctorArgs[2] = Heap.intern(field.getName());
                ctorArgs[3] = field.getType().getClassOop().getAddress();
                ctorArgs[4] = Modifier.translate(field.getModifiers());
                ctorArgs[5] = i; // slot, this is my best guess as to what this means...
                ctorArgs[6] = Heap.NULL_POINTER;
                ctorArgs[7] = Heap.NULL_POINTER;

                Utils.executeMethodAndThrow(ctor, ctorArgs, ctx.getExecutionStack().getThread());
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
            OopClassClass occ = (OopClassClass) Heap.getOop(args[0]);

            return NativeReturn.forInt(Heap.intern(occ.getThisClass().getType().toString()));
        }
    },
    FOR_NAME_0("forName0", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            String className = Utils.toString(Heap.getOopClass(args[0]));
            className = className.replaceAll("\\.", "/");

            try {
                return NativeReturn.forReference(ClassLoader.getDefaultClassLoader().getClassObject(className).getType().getClassOop());
            } catch (AssertionError e) {
                // TODO: proper error, or some other system!
                return NativeReturn.forThrowable(ClassLoader.getDefaultClassLoader().getClassObject("java/lang/ClassNotFoundException").newObject());
            }
        }
    },
    IS_INTERFACE("isInterface", "()Z") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClassClass clazz = (OopClassClass) Heap.getOop(args[0]);

            return NativeReturn.forBool(clazz.getThisClass().hasModifier(Modifier.INTERFACE));
        }
    },
    GET_DECLARED_CONSTRUCTORS_0("getDeclaredConstructors0", "(Z)[Ljava/lang/reflect/Constructor;") {
        @Override
        public NativeReturn execute(int[] args, OperationContext ctx) {
            OopClassClass clazz = (OopClassClass) Heap.getOop(args[0]);
            AbstractClassObject thisClass = clazz.getThisClass();
            ClassObject ctorClass = ClassLoader.getDefaultClassLoader().getClassObject("java/lang/reflect/Constructor");
            ArrayClassObject ctorArray = ArrayClassObject.forType(ArrayType.create(ctorClass.getType(), 1));

            if (thisClass instanceof ArrayClassObject) {
                OopArray ctors = ctorArray.newArray(0); // TODO: cache this?
                return NativeReturn.forReference(ctors);
            } else {
                ClassObject thisClassObj = (ClassObject) thisClass;

                boolean publicOnly = args[1] > 0;

                List<Method> ctors = new ArrayList<>();

                for (Method ctor : ((ClassObject) thisClass).getConstructors()) {
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

                OopArray result = ctorArray.newArray(ctors.size());
                for (int i = 0; i < ctors.size(); i++) {
                    Method ctor = ctors.get(i);
                    OopClass ctorObj = ctorClass.newObject();
                    int[] ctorArgs = ctorCtor.newArgArray();

                    ctorArgs[0] = Heap.allocate(ctorObj);
                    ctorArgs[1] = clazz.getAddress(); // declaring class

                    OopArray paramTypes = classArray.newArray(ctor.getSignature().getArgTypes().size());
                    int j = 0;
                    for (Type type : ctor.getSignature().getArgTypes()) {
                        paramTypes.getFields()[j++] = type.getClassOop().getAddress();
                    }

                    ctorArgs[2] = Heap.allocate(paramTypes);
                    // TODO
                    ctorArgs[3] = Heap.NULL_POINTER; // checkedExceptions
                    ctorArgs[4] = Modifier.translate(ctor.getModifiers());
                    ctorArgs[5] = i; //slot - ??
                    ctorArgs[6] = Heap.intern(ctor.getSignature().toString());
                    ctorArgs[7] = Heap.NULL_POINTER; // annotations
                    ctorArgs[8] = Heap.NULL_POINTER; // parameter annotations

                    Utils.executeMethodAndThrow(ctor, ctorArgs, ctx.getExecutionStack().getThread());

                    result.getFields()[i] = ctorObj.getAddress();
                }

                return NativeReturn.forReference(result);
            }
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
