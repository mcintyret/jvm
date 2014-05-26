package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.MagicClasses;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.clazz.*;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.domain.*;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeImplemntationRegistry;
import com.mcintyret.jvm.core.nativeimpls.ObjectNatives;
import com.mcintyret.jvm.core.nativeimpls.SystemNatives;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.parse.ClassFile;
import com.mcintyret.jvm.parse.ClassFileReader;
import com.mcintyret.jvm.parse.MemberInfo;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.cp.*;

import java.io.IOException;
import java.util.*;

import static com.mcintyret.jvm.core.Assert.assertNotNull;
import static java.util.Arrays.asList;

public class ClassLoader {

    private static final ClassLoader DEFAULT_CLASSLOADER = new ClassLoader();

    private final Map<String, ClassFile> classFiles = new HashMap<>();

    private final Map<FieldKey, Field> fields = new HashMap<>();

    private final Map<MethodKey, Method> methods = new HashMap<>();

    private final Map<String, ClassObject> classes = new HashMap<>();

    protected ClassLoader() {

    }

    public void load(ClassPath classPath) throws IOException {
        ClassFileReader reader = new ClassFileReader();

        for (ClassFileResource resource : classPath) {
            System.out.println("Reading: " + resource.getName());
            ClassFile file = reader.read(resource.getInputStream());
            classFiles.put(getClassName(file.getThisClass(), file.getConstantPool()), file);
        }

        ObjectNatives.registerNatives();
        MagicClasses.registerClass(getClassObject(MagicClasses.JAVA_LANG_OBJECT));
        MagicClasses.registerClass(getClassObject(MagicClasses.JAVA_LANG_CLONEABLE));
        MagicClasses.registerClass(getClassObject(MagicClasses.JAVA_IO_SERIALIZABLE));
        MagicClasses.registerClass(getClassObject(MagicClasses.JAVA_LANG_CLASS));
        MagicClasses.registerClass(getClassObject(MagicClasses.JAVA_LANG_STRING));

        afterInitialLoad();
    }

    private void afterInitialLoad() {
        if (this == DEFAULT_CLASSLOADER) {
            // Do this somewhere else!
//            setSystemOut();
        }
    }

    private void setSystemOut() {
        ClassObject fileDescriptor = getClassObject("java/io/FileDescriptor");
        OopClass outFd = Heap.getOopClass(fileDescriptor.getStaticFieldValues()[1]);

        ClassObject fileOutputStream = getClassObject("java/io/FileOutputStream");
        OopClass fos = fileOutputStream.newObject();
        Heap.allocate(fos);

        OopClass lockObj = getClassObject("java/lang/Object").newObject();
        Heap.allocate(lockObj);

        fos.getFields()[0] = outFd.getAddress();
        fos.getFields()[4] = lockObj.getAddress();

        ClassObject bufferedOutputStream = getClassObject("java/io/BufferedOutputStream");
        OopClass bos = bufferedOutputStream.newObject();
        Method bosConstructor = bufferedOutputStream.findMethod("<init>", "(Ljava/io/OutputStream)V", false);
        Utils.executeMethod(bosConstructor, new int[]{Heap.allocate(bos), fos.getAddress()});

        ClassObject printStream = getClassObject("java/io/PrintStream");
        OopClass ps = printStream.newObject();
        Method psConstructor = printStream.findMethod("<init>", "(ZLjava/io/OutputStream)V", false);
        Utils.executeMethod(psConstructor, new int[]{Heap.allocate(ps), bos.getAddress()});

        SystemNatives.SET_OUT_0.execute(new int[]{ps.getAddress()}, null);
    }

    public ClassObject getClassObject(String className) {
        ClassObject co = classes.get(className);
        if (co == null) {
            System.out.println("Loading: " + className);
            ClassFile file = assertNotNull(classFiles.remove(className), "No class file for " + className);

            ClassObject parent = null;
            int parentIndex = file.getSuperClass();
            if (parentIndex != 0) {
                String parentClass = getClassName(parentIndex, file.getConstantPool());
                parent = getClassObject(parentClass);
            }

            co = makeClassObject(className, file, parent);
            classes.put(className, co);
            executeStaticInitMethod(co);
            System.out.println("Done Loading: " + className);
        }
        return co;
    }

    // Superclasses and interfaces are recursively loaded
    private ClassObject makeClassObject(String className, ClassFile file, ClassObject parent) {
        boolean isInterface = file.hasModifier(Modifier.INTERFACE);

        // Load all interfaces first
        ClassObject[] ifaces = new ClassObject[file.getInterfaces().length];
        for (int i = 0; i < ifaces.length; i++) {
            ifaces[i] = getClassObject(getClassName(file.getInterfaces()[i], file.getConstantPool()));
        }

        // Methods - sorting out the VTable
        NonArrayType type = NonArrayType.forClass(className);

        List<Method> staticMethods = new ArrayList<>();
        List<MethodInfoAndSig> instanceMethods = new LinkedList<>();

        for (MemberInfo method : file.getMethods()) {
            if (method.hasModifier(Modifier.STATIC)) {
                staticMethods.add(translateMethod(new MethodInfoAndSig(method, file.getConstantPool(), className), false));
            } else {
                instanceMethods.add(new MethodInfoAndSig(method, file.getConstantPool(), className));
            }
        }

        instanceMethods.sort(PRIVATE_LAST_COMPARATOR);
        Method[] parentMethods = parent != null && !isInterface ? parent.getInstanceMethods() : null;
        List<Method> orderedMethods = new ArrayList<>();

        if (parentMethods != null) {
            for (Method parentMethod : parentMethods) {
                if (parentMethod.hasModifier(Modifier.PRIVATE)) {
                    // we known nothing after this point can be overridden
                    break;
                }
                ListIterator<MethodInfoAndSig> li = instanceMethods.listIterator();
                boolean overridden = false;
                while (li.hasNext()) {
                    MethodInfoAndSig mis = li.next();
                    if (mis.sig.equals(parentMethod.getSignature())) {
                        // Override!!
                        orderedMethods.add(translateMethod(mis, false));
                        li.remove();
                        overridden = true;
                        break;
                    }
                }
                if (!overridden) {
                    // copy down the superClass's Method
                    orderedMethods.add(parentMethod);
                }
            }
        }

        for (MethodInfoAndSig instanceMethod : instanceMethods) {
            orderedMethods.add(translateMethod(instanceMethod, isInterface));
        }

        Field[] translatedStaticFields;
        Field[] translatedInstanceFields;

        if (isInterface) {
            translatedStaticFields = translatedInstanceFields = new Field[0];
        } else {

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

            translatedStaticFields = translateFields(new ArrayList<>(staticFields.size()), staticFields, file.getConstantPool());

            translatedInstanceFields = translateFields(parent == null ? new ArrayList<>() :
                    new ArrayList<>(asList(parent.getInstanceFields())), instanceFields, file.getConstantPool());
        }


        ClassObject co = new ClassObject(
                type,
                file.getModifiers(),
                parent,
                ifaces,
                new ConstantPool(file.getConstantPool(), this),
                orderedMethods.toArray(new Method[orderedMethods.size()]),
                staticMethods.toArray(new Method[staticMethods.size()]),
                translatedInstanceFields,
                translatedStaticFields,
                this);

        registerInterfaceImplementations(co);

        cacheFields(co.getInstanceFields(), className);
        cacheFields(co.getStaticFields(), className);

        cacheMethods(co.getInstanceMethods(), className);
        cacheMethods(co.getStaticMethods(), className);

        translateSimpleConstantPool(file.getConstantPool());

        return co;
    }

    private void registerInterfaceImplementations(ClassObject co) {
        // TODO: make more efficient
        ClassObject obj = co;
        String className = co.getClassName();
        Set<ClassObject> allInterfaces = new HashSet<>();

        do {
            addInterfaces(obj, allInterfaces);
            obj = obj.getSuperClass();
        } while (obj != null);

        for (ClassObject iface : allInterfaces) {
            for (Method ifaceMethod : iface.getInstanceMethods()) {
                for (Method implementation : co.getInstanceMethods()) {
                    if (!implementation.hasModifier(Modifier.ABSTRACT) && implementation.hasModifier(Modifier.PUBLIC) && ifaceMethod.getSignature().equals(implementation.getSignature())) {
                        // Interface implementation!
                        InterfaceMethod imr = (InterfaceMethod) ifaceMethod;
                        imr.registerMethodForImplementation(className, implementation);
                        break;
                    }
                }
            }
        }
    }

    private void addInterfaces(ClassObject classObject, Set<ClassObject> interfaces) {
        for (ClassObject iface : classObject.getInterfaces()) {
            interfaces.add(iface);
            addInterfaces(iface, interfaces);
        }
    }

    private void executeStaticInitMethod(ClassObject co) {
        Method staticInit = co.findMethod("<clinit>", "()V", true);
        if (staticInit != null) {
            Utils.executeMethod(staticInit, new int[staticInit.getCode().getMaxLocals()]);
        }
    }

    private static Method translateMethod(MethodInfoAndSig mis, boolean isInterface) {
        MemberInfo info = mis.mi;

        if (isInterface) {
            return new InterfaceMethod(info.getModifiers(), info.getAttributes(), mis.sig);
        } else {
            if (info.hasModifier(Modifier.NATIVE)) {
                NativeImplementation nativeImplementation = NativeImplemntationRegistry.getNativeExecution(mis.className, mis.sig);
                if (nativeImplementation == null) {
//                throw new IllegalStateException("No NativeImplementation registered for " + mis.className + "." + mis.sig);
                    System.out.println("NATIVE METHOD MISSING: " + mis.className + "." + mis.sig);
                }
                return new NativeMethod(info.getModifiers(), info.getAttributes(), mis.sig, nativeImplementation);
            } else {
                return new Method(info.getModifiers(), info.getAttributes(), mis.sig);
            }
        }
    }

    private void cacheMethods(Method[] methods, String className) {
        for (Method method : methods) {
            MethodKey key = new MethodKey(className, method.getSignature());
            this.methods.put(key, method);
        }
    }

    private void cacheFields(Field[] fields, String className) {
        for (Field field : fields) {
            FieldKey key = new FieldKey(className, field.getName(), field.getType());
            this.fields.put(key, field);
        }
    }


    private void translateSimpleConstantPool(Object[] constantPool) {
        for (int i = 1; i < constantPool.length; i++) {
            Object obj = constantPool[i];
            if (obj instanceof CpInt) {
                constantPool[i] = ((CpInt) obj).getIntBits();
            } else if (obj instanceof CpFloat) {
                constantPool[i] = ((CpFloat) obj).getFloatBits();
            } else if (obj instanceof CpLong) {
                CpLong cpLong = (CpLong) obj;
                constantPool[i] = Utils.toLong(cpLong.getHighBits(), cpLong.getLowBits());
            } else if (obj instanceof CpDouble) {
                CpDouble cpDouble = (CpDouble) obj;
                constantPool[i] = Utils.toLong(cpDouble.getHighBits(), cpDouble.getLowBits());
            }
        }
    }

    private static Field[] translateFields(List<Field> fields, List<MemberInfo> fomis, Object[] constantPool) {
        int offset = 0;
        if (!fields.isEmpty() && !fomis.isEmpty()) {
            Field lastField = fields.get(fields.size() - 1);
            offset = lastField.getOffset() + lastField.getType().getSimpleType().getWidth();
        }

        for (MemberInfo field : fomis) {
            Type type = Types.parseType((String) constantPool[field.getDescriptorIndex()]);
            String name = (String) constantPool[field.getNameIndex()];
            fields.add(new Field(field.getModifiers(), field.getAttributes(), name, type, offset));
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
            return b.mi.hasModifier(Modifier.PRIVATE) ? 0 : 1;
        } else {
            return !b.mi.hasModifier(Modifier.PRIVATE) ? 0 : -1;
        }
    };

    public AbstractClassObject translate(CpClass cpClass, Object[] constantPool) {
        String className = (String) constantPool[cpClass.getNameIndex()];
        return className.startsWith("[") ?
                ArrayClassObject.forType((ArrayType) Types.parseType(className)) :
                getClassObject(className);
    }

    public Field translate(CpFieldReference cfr, Object[] constantPool) {
        ClassObject co = findClassObject(cfr, constantPool);

        NameAndType nat = (NameAndType) constantPool[cfr.getNameAndTypeIndex()];
        String name = (String) constantPool[nat.getNameIndex()];
        Type type = Types.parseType((String) constantPool[nat.getDescriptorIndex()]);

        FieldKey key = new FieldKey(co.getClassName(), name, type);
        return assertNotNull(fields.get(key), "No FieldReference for " + key);
    }

    public Method translate(CpMethodReference cmr, Object[] constantPool) {
        ClassObject co = findClassObject(cmr, constantPool);

        NameAndType nat = (NameAndType) constantPool[cmr.getNameAndTypeIndex()];
        String name = (String) constantPool[nat.getNameIndex()];
        String descriptor = (String) constantPool[nat.getDescriptorIndex()];

        MethodSignature methodSignature = MethodSignature.parse(name, descriptor);

        MethodKey md = new MethodKey(co.getClassName(), methodSignature);
        return assertNotNull(methods.get(md), "No MethodReference for " + md);
    }

    private ClassObject findClassObject(CpReference ref, Object[] constantPool) {
        int classIndex = ref.getClassIndex();
        if (constantPool[classIndex] instanceof ClassObject) {
            return (ClassObject) constantPool[classIndex];
        }
        String clazz = getClassName(classIndex, constantPool);

        if (clazz.startsWith("[")) {
            // Array classes will only ever have Object methods called on them, so this *should* work. Feels hacky though.
            return classes.get("java/lang/Object");
        }

        ClassObject co = getClassObject(clazz);
        constantPool[classIndex] = co;
        return co;
    }

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

        protected final String className;

        private final MethodSignature signature;

        protected MethodKey(String className, MethodSignature signature) {
            this.className = className;
            this.signature = signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodKey methodKey = (MethodKey) o;

            if (!className.equals(methodKey.className)) return false;
            if (!signature.equals(methodKey.signature)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return className + "." + signature;
        }

    }

    private static class FieldKey {

        private final String className;

        private final String name;

        private final Type type;

        private FieldKey(String className, String name, Type type) {
            this.className = className;
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FieldKey fieldKey = (FieldKey) o;

            if (!className.equals(fieldKey.className)) return false;
            if (!name.equals(fieldKey.name)) return false;
            if (!type.equals(fieldKey.type)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "FieldKey{" +
                    "class=" + className +
                    ", type=" + type +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        return DEFAULT_CLASSLOADER;
    }

}
