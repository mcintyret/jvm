package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.ByteCode;
import com.mcintyret.jvm.core.ClassObject;
import com.mcintyret.jvm.core.Field;
import com.mcintyret.jvm.core.Method;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.constantpool.FieldReference;
import com.mcintyret.jvm.core.constantpool.MethodReference;
import com.mcintyret.jvm.core.domain.MethodSignature;
import com.mcintyret.jvm.core.domain.ReferenceType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.domain.Types;
import com.mcintyret.jvm.parse.ClassFile;
import com.mcintyret.jvm.parse.ClassFileReader;
import com.mcintyret.jvm.parse.FieldOrMethodInfo;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attribute;
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

    private Map<String, ClassFile> classFiles = new HashMap<>();

    private final Map<FieldDetails, FieldReference> fields = new HashMap<>();

    private final Map<MethodDetails, MethodReference> methods = new HashMap<>();

    private final Map<String, ClassObject> classes = new HashMap<>();

    public void load(Iterable<InputStream> classFileStreams) throws IOException {
        ClassFileReader reader = new ClassFileReader();

        for (InputStream stream : classFileStreams) {
            ClassFile file = reader.read(stream);
            classFiles.put(getClassName(file.getThisClass(), file.getConstantPool()), file);
        }

        while (!classFiles.isEmpty()) {
            load(classFiles.keySet().iterator().next());
        }
    }

    private ClassObject load(String className) {
        ClassObject co = classes.get(className);
        if (co == null) {
            ClassFile file = assertNotNull(classFiles.remove(className));

            ClassObject parent = null;
            int parentIndex = file.getSuperClass();
            if (parentIndex != 0) {
                String parentClass = getClassName(parentIndex, file.getConstantPool());
                parent = load(parentClass);
            }

            co = makeClassObject(file, parent);
            classes.put(className, co);
        }
        return co;
    }

    private ClassObject makeClassObject(ClassFile file, ClassObject parent) {
        // Methods - sorting out the VTable
        ReferenceType type = ReferenceType.forClass(getClassName(file.getThisClass(), file.getConstantPool()));

        List<Method> staticMethods = new ArrayList<>();
        List<FomiAndMethodSig> instanceMethods = new LinkedList<>();

        for (FieldOrMethodInfo method : file.getMethods()) {
            if (method.hasModifier(Modifier.STATIC)) {
                staticMethods.add(translateMethod(new FomiAndMethodSig(method, file.getConstantPool())));
            } else {
                instanceMethods.add(new FomiAndMethodSig(method, file.getConstantPool()));
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
                ListIterator<FomiAndMethodSig> li = instanceMethods.listIterator();
                boolean overridden = false;
                while (li.hasNext()) {
                    FomiAndMethodSig fms = li.next();
                    if (fms.sig.equals(parentmethod.getSignature())) {
                        // Override!!
                        orderedMethods.add(translateMethod(fms));
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

        for (FomiAndMethodSig instanceMethod : instanceMethods) {
            orderedMethods.add(translateMethod(instanceMethod));
        }

        // Fields
        List<FieldOrMethodInfo> staticFields = new ArrayList<>();
        List<FieldOrMethodInfo> instanceFields = new ArrayList<>();
        for (FieldOrMethodInfo field : file.getFields()) {
            if (field.hasModifier(Modifier.STATIC)) {
                staticFields.add(field);
            } else {
                instanceFields.add(field);
            }
        }

        Field[] translatedStaticFields = translateFields(new ArrayList<>(staticFields.size()), staticFields, file.getConstantPool());

        Field[] translatedInstanceFields = translateFields(parent == null ? new ArrayList<>() :
            new ArrayList<>(asList(parent.getInstanceFields())), instanceFields, file.getConstantPool());


        ClassObject co = new ClassObject(
            type,
            parent,
            translateConstantPool(file.getConstantPool()),
            orderedMethods.toArray(new Method[orderedMethods.size()]),
            staticMethods.toArray(new Method[staticMethods.size()]),
            translatedInstanceFields,
            translatedStaticFields
        );

        cacheFields(co, co.getInstanceFields());
        cacheFields(co, co.getStaticFields());
        cacheMethods(co, co.getInstanceMethods());
        cacheMethods(co, co.getStaticMethods());

        return co;
    }

    private void cacheMethods(ClassObject co, Method[] methods) {
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            MethodReference ref = new MethodReference(co, i);
            MethodDetails details = new MethodDetails(co.getType(), method.getSignature());
            this.methods.put(details, ref);
        }
    }

    private void cacheFields(ClassObject co, Field[] fields) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            FieldReference ref = new FieldReference(co, i);
            FieldDetails details = new FieldDetails(co.getType(), field.getName(), field.getType());
            this.fields.put(details, ref);
        }
    }

    private ConstantPool translateConstantPool(Object[] constantPool) {
        Object[] translated = new Object[constantPool.length];
        for (int i = 1; i < constantPool.length; i++) {
            Object obj = constantPool[i];

            if (obj instanceof CpString) {
                translated[i] = ((CpString) obj).getStringIndex();
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
                // Not strictly needed but probably handy
                translated[i] = ReferenceType.forClass((String) constantPool[((CpClass) obj).getNameIndex()]);
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

                FieldDetails fd = new FieldDetails(co.getType(), name, type);
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

                MethodDetails md = new MethodDetails(co.getType(), methodSignature);
                MethodReference mr = assertNotNull(methods.get(md), "No MethodReference for " + md);
                translated[i] = mr;
            }
            // TODO: interface methods!
        }

        return new ConstantPool(translated);
    }

    private static Field[] translateFields(List<Field> fields, List<FieldOrMethodInfo> fomis, Object[] constantPool) {
        int offset = 0;
        if (!fields.isEmpty()) {
            Field lastField = fields.get(fields.size() - 1);
            offset = lastField.getOffset() + lastField.getType().getSimpleType().getWidth();
        }

        for (FieldOrMethodInfo field : fomis) {
            Type type = Types.parseType((String) constantPool[field.getDescriptorIndex()]);
            String name = (String) constantPool[field.getNameIndex()];
            fields.add(new Field(type, name, offset));
            offset += type.getSimpleType().getWidth();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    private static Method translateMethod(FomiAndMethodSig fms) {
        FieldOrMethodInfo info = fms.fomi;
        byte[] code = null;

        for (Attribute a : info.getAttributes()) {
            if (a.getType() == AttributeType.CODE) {
                code = ((Code) a).getCode();
                break;
            }
        }

        // code could still be null if this is an ABSTRACT or NATIVE method
        return new Method(new ByteCode(code), fms.sig, info.getModifiers());
    }


    private static String getClassName(int index, Object[] constantPool) {
        CpClass cpClass = (CpClass) constantPool[index];
        return (String) constantPool[cpClass.getNameIndex()];
    }

    private static final Comparator<FieldOrMethodInfo> PRIVATE_LAST_COMPARATOR = (a, b) -> {
        if (a.hasModifier(Modifier.PRIVATE)) {
            return b.hasModifier(Modifier.PRIVATE) ? 0 : -1;
        } else {
            return !b.hasModifier(Modifier.PRIVATE) ? 0 : 1;
        }
    };

    private static class FomiAndMethodSig {

        private final FieldOrMethodInfo fomi;

        private final MethodSignature sig;

        private FomiAndMethodSig(FieldOrMethodInfo fomi, Object[] constantPool) {
            this.fomi = fomi;
            String name = (String) constantPool[fomi.getNameIndex()];
            String descriptor = (String) constantPool[fomi.getDescriptorIndex()];
            sig = MethodSignature.parse(name, descriptor);
        }
    }

    private static class MethodDetails {

        protected final ReferenceType clazz;

        private final MethodSignature signature;

        protected MethodDetails(ReferenceType clazz, MethodSignature signature) {
            this.clazz = clazz;
            this.signature = signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodDetails that = (MethodDetails) o;

            if (!clazz.equals(that.clazz)) return false;
            if (!signature.equals(that.signature)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "MethodDetails{" +
                "clazz=" + clazz +
                ", signature=" + signature +
                '}';
        }
    }

    private static class FieldDetails {

        protected final ReferenceType clazz;

        protected final String name;

        private final Type type;

        private FieldDetails(ReferenceType clazz, String name, Type type) {
            this.clazz = clazz;
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FieldDetails that = (FieldDetails) o;

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
            return "FieldDetails{" +
                "clazz=" + clazz +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
        }
    }

}
