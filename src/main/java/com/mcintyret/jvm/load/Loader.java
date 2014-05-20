package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.ByteCode;
import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.NativeExecution;
import com.mcintyret.jvm.core.NativeExecutionRegistry;
import com.mcintyret.jvm.core.NativeMethod;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.constantpool.InterfaceMethodReference;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.domain.Types;
import com.mcintyret.jvm.parse.ClassFile;
import com.mcintyret.jvm.parse.ClassFileReader;
import com.mcintyret.jvm.parse.MemberInfo;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.AttributeType;
import com.mcintyret.jvm.parse.attribute.Code;
import com.mcintyret.jvm.parse.cp.CpClass;
import com.mcintyret.jvm.parse.cp.CpDouble;
import com.mcintyret.jvm.parse.cp.CpFieldReference;
import com.mcintyret.jvm.parse.cp.CpFloat;
import com.mcintyret.jvm.parse.cp.CpInt;
import com.mcintyret.jvm.parse.cp.CpLong;
import com.mcintyret.jvm.parse.cp.CpMethodReference;
import com.mcintyret.jvm.parse.cp.CpString;
import com.mcintyret.jvm.parse.cp.NameAndType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.mcintyret.jvm.core.Assert.assertNotNull;
import static java.util.Arrays.asList;

public class Loader {

    private final Map<String, ClassFile> classFiles = new HashMap<>();

    private final Map<FieldKey, FieldReference> fields = new HashMap<>();

    private final Map<MethodKey, MethodReference> methods = new HashMap<>();

    private final Map<String, ClassObject> classes = new HashMap<>();

    public ClassObject getClassObject(String className) {
        return classes.get(className);
    }

    public void load(Iterable<InputStream> classFileStreams) throws IOException {
        ClassFileReader reader = new ClassFileReader();

        for (InputStream stream : classFileStreams) {
            ClassFile file = reader.read(stream);
            classFiles.put(getClassName(file.getThisClass(), file.getConstantPool()), file);
        }

        // Needs to be done first so we can handle String literals
        // TODO this is rubbish
        load(Heap.STRING_CLASS_NAME);

        while (!classFiles.isEmpty()) {
            load(classFiles.keySet().iterator().next());
        }
    }

    private ClassObject load(String className) {
        ClassObject co = classes.get(className);
        if (co == null) {
            System.out.println("Loading: " + className);
            ClassFile file = assertNotNull(classFiles.remove(className));

            ClassObject parent = null;
            int parentIndex = file.getSuperClass();
            if (parentIndex != 0) {
                String parentClass = getClassName(parentIndex, file.getConstantPool());
                parent = load(parentClass);
            }

            co = makeClassObject(className, file, parent);
        }
        return co;
    }

    private ClassObject makeClassObject(String className, ClassFile file, ClassObject parent) {
        // Load all interfaces first
        ClassObject[] ifaces = new ClassObject[file.getInterfaces().length];
        for (int i = 0; i < ifaces.length; i++) {
            ifaces[0] = load(getClassName(file.getInterfaces()[i], file.getConstantPool()));
        }

        // Methods - sorting out the VTable
        ReferenceType type = ReferenceType.forClass(className);

        List<Method> staticMethods = new ArrayList<>();
        List<MethodInfoAndSig> instanceMethods = new LinkedList<>();

        for (MemberInfo method : file.getMethods()) {
            if (method.hasModifier(Modifier.STATIC)) {
                staticMethods.add(translateMethod(new MethodInfoAndSig(method, file.getConstantPool(), className)));
            } else {
                instanceMethods.add(new MethodInfoAndSig(method, file.getConstantPool(), className));
            }
        }

        instanceMethods.sort(PRIVATE_LAST_COMPARATOR);
        Method[] parentMethods = parent != null ? parent.getInstanceMethods() : null;
        List<Method> orderedMethods = new ArrayList<>();

        if (parentMethods != null) {
            for (Method parentmethod : parentMethods) {
                if (parentmethod.hasModifier(Modifier.PRIVATE)) {
                    // we known nothing after this point can be overridden
                    break;
                }
                ListIterator<MethodInfoAndSig> li = instanceMethods.listIterator();
                boolean overridden = false;
                while (li.hasNext()) {
                    MethodInfoAndSig mis = li.next();
                    if (mis.sig.equals(parentmethod.getSignature())) {
                        // Override!!
                        orderedMethods.add(translateMethod(mis));
                        li.remove();
                        overridden = true;
                        break;
                    }
                }
                if (!overridden) {
                    // copy down the parent's Method
                    orderedMethods.add(parentmethod);
                }
            }
        }

        for (MethodInfoAndSig instanceMethod : instanceMethods) {
            orderedMethods.add(translateMethod(instanceMethod));
        }

        for (ClassObject iface : ifaces) {
            for (Method ifaceMethod : iface.getInstanceMethods()) {
                for (Method implementation : orderedMethods) {
                    if (ifaceMethod.getSignature().equals(implementation.getSignature())) {
                        // Interface implementation!
                        InterfaceMethodReference imr = (InterfaceMethodReference) ifaceMethod.getMethodReference();
                        imr.registerMethodForImplementation(className, implementation);
                        break;
                    }
                }
            }
        }


        // Fields
        List<MemberInfo> staticFields = new ArrayList<>();
        List<MemberInfo> instanceFields = new ArrayList<>();
        for (MemberInfo field : file.getFields()) {
            if (field.hasModifier(Modifier.STATIC)) {
                staticFields.add(field);
            } else {
                instanceFields.add(field);
            }
        }

        Field[] translatedStaticFields = translateFields(new ArrayList<>(staticFields.size()), staticFields, file.getConstantPool());

        Field[] translatedInstanceFields = translateFields(parent == null ? new ArrayList<>() :
            new ArrayList<>(asList(parent.getInstanceFields())), instanceFields, file.getConstantPool());

        Object[] newConstantPool = new Object[file.getConstantPool().length];

        ClassObject co = new ClassObject(
            type,
            file.getModifiers(),
            parent,
            new ConstantPool(newConstantPool),
            orderedMethods.toArray(new Method[orderedMethods.size()]),
            staticMethods.toArray(new Method[staticMethods.size()]),
            translatedInstanceFields,
            translatedStaticFields
        );

        cacheFields(co, co.getInstanceFields());
        cacheFields(co, co.getStaticFields());

        cacheMethods(co, co.getInstanceMethods(), false);
        cacheMethods(co, co.getStaticMethods(), true);

        classes.put(className, co);

        if (className.equals(Heap.STRING_CLASS_NAME)) {
            Heap.setStringClass(co);
        }

        translateConstantPool(file.getConstantPool(), newConstantPool);

        return co;
    }

    private static Method translateMethod(MethodInfoAndSig mis) {
        MemberInfo info = mis.mi;

        if (info.hasModifier(Modifier.NATIVE)) {
            NativeExecution nativeExecution = NativeExecutionRegistry.getNativeExecution(mis.className, mis.sig);
            if (nativeExecution == null) {
                throw new IllegalStateException("No NativeExecution registered for " + mis.className + "." + mis.sig);
            }
            return new NativeMethod(mis.sig, info.getModifiers(), nativeExecution);
        } else {
            byte[] byteCode = null;

            Code code = (Code) info.getAttributes().getAttribute(AttributeType.CODE);
            if (code != null) {
                byteCode = code.getCode();
            }
            // code could still be null if this is an ABSTRACT or NATIVE method
            return new Method(new ByteCode(byteCode), mis.sig, info.getModifiers(), code == null ? -1 : code.getMaxLocals());
        }
    }

    private void cacheMethods(ClassObject co, Method[] methods, boolean isStatic) {
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            MethodReference ref = co.hasAttribute(Modifier.INTERFACE) ?
                new InterfaceMethodReference(co, i) :
                new MethodReference(co, i, isStatic);
            MethodKey details = new MethodKey(co.getType(), method.getSignature());
            this.methods.put(details, ref);
            method.setMethodReference(ref);
        }
    }

    private void cacheFields(ClassObject co, Field[] fields) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            FieldReference ref = new FieldReference(co, i);
            FieldKey details = new FieldKey(co.getType(), field.getName(), field.getType());
            this.fields.put(details, ref);
        }
    }

    private void translateConstantPool(Object[] constantPool, Object[] translated) {
        for (int i = 1; i < constantPool.length; i++) {
            Object obj = constantPool[i];

            if (obj instanceof CpString) {
                int index = ((CpString) obj).getStringIndex();
                String string = (String) constantPool[index];
                translated[i] = Heap.intern(string);
            } else if (obj instanceof CpInt) {
                translated[i] = ((CpInt) obj).getIntBits();
            } else if (obj instanceof CpFloat) {
                translated[i] = ((CpFloat) obj).getFloatBits();
            } else if (obj instanceof CpLong) {
                CpLong cpLong = (CpLong) obj;
                translated[i] = Utils.toLong(cpLong.getHighBits(), cpLong.getLowBits());
            } else if (obj instanceof CpDouble) {
                CpDouble cpDouble = (CpDouble) obj;
                translated[i] = Utils.toLong(cpDouble.getHighBits(), cpDouble.getLowBits());
            } else if (obj instanceof CpClass) {
                String className = (String) constantPool[((CpClass) obj).getNameIndex()];
                if (!className.startsWith("[")) {
                    // Don't want to load the array classes here...
                    translated[i] = load(className);
                }
            } else if (obj instanceof CpFieldReference) {
                CpFieldReference cfr = (CpFieldReference) obj;

                String clazz = getClassName(cfr.getClassIndex(), constantPool);
                ClassObject co = classes.get(clazz);
                if (co == null) {
                    co = load(clazz);
                }

                NameAndType nat = (NameAndType) constantPool[cfr.getNameAndTypeIndex()];
                String name = (String) constantPool[nat.getNameIndex()];
                Type type = Types.parseType((String) constantPool[nat.getDescriptorIndex()]);

                FieldKey fd = new FieldKey(co.getType(), name, type);
                FieldReference fr = assertNotNull(fields.get(fd), "No FieldReference for " + fd);
                translated[i] = fr;
            } else if (obj instanceof CpMethodReference) {
                CpMethodReference cmr = (CpMethodReference) obj;

                String clazz = getClassName(cmr.getClassIndex(), constantPool);
                ClassObject co = classes.get(clazz);
                if (co == null) {
                    co = load(clazz);
                }

                NameAndType nat = (NameAndType) constantPool[cmr.getNameAndTypeIndex()];
                String name = (String) constantPool[nat.getNameIndex()];
                String descriptor = (String) constantPool[nat.getDescriptorIndex()];

                MethodSignature methodSignature = MethodSignature.parse(name, descriptor);

                MethodKey md = new MethodKey(co.getType(), methodSignature);
                MethodReference mr = assertNotNull(methods.get(md), "No MethodReference for " + md);
                translated[i] = mr;
            } else if (obj instanceof String || obj instanceof NameAndType) {
                // Not dealt with directly
            } else {
                throw new AssertionError("Unexpected type: " + obj.getClass());
            }
        }
    }

    private static Field[] translateFields(List<Field> fields, List<MemberInfo> fomis, Object[] constantPool) {
        int offset = 0;
        if (!fields.isEmpty()) {
            Field lastField = fields.get(fields.size() - 1);
            offset = lastField.getOffset() + lastField.getType().getSimpleType().getWidth();
        }

        for (MemberInfo field : fomis) {
            Type type = Types.parseType((String) constantPool[field.getDescriptorIndex()]);
            String name = (String) constantPool[field.getNameIndex()];
            fields.add(new Field(type, name, offset));
            offset += type.getSimpleType().getWidth();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    private static String getClassName(int index, Object[] constantPool) {
        CpClass cpClass = (CpClass) constantPool[index];
        return (String) constantPool[cpClass.getNameIndex()];
    }

    private static final Comparator<MethodInfoAndSig> PRIVATE_LAST_COMPARATOR = (a, b) -> {
        if (a.mi.hasModifier(Modifier.PRIVATE)) {
            return b.mi.hasModifier(Modifier.PRIVATE) ? 0 : -1;
        } else {
            return !b.mi.hasModifier(Modifier.PRIVATE) ? 0 : 1;
        }
    };

    private static class MethodInfoAndSig {

        private final String className;

        private final MemberInfo mi;

        private final MethodSignature sig;

        private MethodInfoAndSig(MemberInfo mi, Object[] constantPool, String className) {
            this.mi = mi;
            this.className = className;
            String name = (String) constantPool[mi.getNameIndex()];
            String descriptor = (String) constantPool[mi.getDescriptorIndex()];
            sig = MethodSignature.parse(name, descriptor);
        }
    }

    private static class MethodKey {

        protected final ReferenceType clazz;

        private final MethodSignature signature;

        protected MethodKey(ReferenceType clazz, MethodSignature signature) {
            this.clazz = clazz;
            this.signature = signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodKey that = (MethodKey) o;

            return clazz.equals(that.clazz) && signature.equals(that.signature);

        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "MethodKey{" +
                "clazz=" + clazz +
                ", signature=" + signature +
                '}';
        }
    }

    private static class FieldKey {

        protected final ReferenceType clazz;

        protected final String name;

        private final Type type;

        private FieldKey(ReferenceType clazz, String name, Type type) {
            this.clazz = clazz;
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FieldKey that = (FieldKey) o;

            if (!clazz.equals(that.clazz)) return false;
            if (!name.equals(that.name)) return false;
            if (!type.equals(that.type)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + type.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "FieldKey{" +
                "clazz=" + clazz +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
        }
    }


}
