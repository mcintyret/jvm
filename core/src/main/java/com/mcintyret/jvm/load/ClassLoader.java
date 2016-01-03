package com.mcintyret.jvm.load;

import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.InterfaceMethod;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.NativeMethod;
import com.mcintyret.jvm.core.constantpool.ConstantPool;
import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Thread;
import com.mcintyret.jvm.core.exec.Threads;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementation;
import com.mcintyret.jvm.core.nativeimpls.NativeImplementationRegistry;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.nativeimpls.ObjectNatives;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.type.NonArrayType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.type.Types;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.parse.ClassFile;
import com.mcintyret.jvm.parse.ClassFileReader;
import com.mcintyret.jvm.parse.MemberInfo;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.cp.CpClass;
import com.mcintyret.jvm.parse.cp.CpDouble;
import com.mcintyret.jvm.parse.cp.CpFieldReference;
import com.mcintyret.jvm.parse.cp.CpFloat;
import com.mcintyret.jvm.parse.cp.CpInt;
import com.mcintyret.jvm.parse.cp.CpLong;
import com.mcintyret.jvm.parse.cp.CpMethodReference;
import com.mcintyret.jvm.parse.cp.CpReference;
import com.mcintyret.jvm.parse.cp.NameAndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;

public class ClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ClassLoader.class);

    private static final Comparator<MethodInfoAndSig> PRIVATE_LAST_COMPARATOR = (a, b) -> {
        if (a.mi.hasModifier(Modifier.PRIVATE)) {
            return b.mi.hasModifier(Modifier.PRIVATE) ? 0 : 1;
        } else {
            return !b.mi.hasModifier(Modifier.PRIVATE) ? 0 : -1;
        }
    };

    private static final ClassLoader BOOTSTRAP_CLASSLOADER = makeBootstrapClassLoader();

    private static ClassLoader makeBootstrapClassLoader() {
        String libJarPath = System.getProperty("java.jvm.home") + "/jre/lib/rt.jar";
        try {
            return new ClassLoader(new ZipClassPath(libJarPath));
        } catch (IOException e) {
            throw new IllegalStateException("Java standard library not found at " + libJarPath);
        }
    }

    private final Map<String, ClassFile> classFiles = new HashMap<>();

    private final Map<FieldKey, Field> fields = new ConcurrentHashMap<>();

    private final Map<MethodKey, Method> methods = new ConcurrentHashMap<>();

    private final Map<String, ClassObject> classes = new ConcurrentHashMap<>();

    protected ClassLoader() {

    }

    protected ClassLoader(ClassPath classPath) throws IOException {
        load(classPath);
    }

    public void load(ClassPath classPath) throws IOException {
        ClassFileReader reader = new ClassFileReader();
        boolean firstLoad = classFiles.isEmpty();

        for (ClassFileResource resource : classPath) {
            LOG.debug("Reading: {}", resource.getName());
            ClassFile file = reader.read(resource.getInputStream());
            classFiles.put(getClassName(file.getThisClass(), file.getConstantPool()), file);
        }

        if (firstLoad) {
            ObjectNatives.registerNatives();
        }
    }

    public void afterInitialLoad() {
        if (this == BOOTSTRAP_CLASSLOADER) {
            // Do this somewhere else!
//            setSystemProperties();
//            setSystemOut();

            ClassObject system = getClassObject("java/lang/System");
            Method init = system.findMethod("initializeSystemClass", true);
            Utils.executeMethodAndThrow(init, init.newArgArray(), Runner.MAIN_THREAD);
        }
    }

    public ClassObject getClassObject(String className) {
        ClassObject co = classes.get(className);
        if (co == null) {
            ClassFile file = classFiles.get(className);

            if (file == null) {
                /*
                    Could be null for 2 reasons:
                    1) Another thread got here first and initialized this class, which which case we can take the ClassObject
                    2) The provided className is invalid
                 */

                co = classes.get(className);

                if (co == null) {
                    throw new IllegalArgumentException("No class file for " + className);
                }
            } else {
                synchronized (file) {
                    // double-check locking
                    co = classes.get(className);

                    if (co == null) {
                        // we're the first - do the load
                        LOG.debug("Loading: {}", className);

                        ClassObject parent = null;
                        int parentIndex = file.getSuperClass();
                        if (parentIndex != 0) {
                            String parentClass = getClassName(parentIndex, file.getConstantPool());
                            parent = getClassObject(parentClass);
                        }

                        co = makeClassObject(className, file, parent);
                        classes.put(className, co);
                        classFiles.remove(className);
                        executeStaticInitMethod(co);
                        LOG.debug("Done Loading: {}", className);
                    }
                }
            }
        }
        return co;
    }

    public Collection<ClassObject> getLoadedClasses() {
        return Collections.unmodifiableCollection(classes.values());
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
        List<Method> constructors = new ArrayList<>();
        List<MethodInfoAndSig> instanceMethods = new LinkedList<>();

        int staticOffset = 0; // Probably never needed, but for completeness
        for (MemberInfo method : file.getMethods()) {
            if (method.hasModifier(Modifier.STATIC)) {
                staticMethods.add(translateMethod(new MethodInfoAndSig(method, file.getConstantPool(), className), false, staticOffset++));
            } else {
                MethodInfoAndSig mis = new MethodInfoAndSig(method, file.getConstantPool(), className);
                if (!isInterface && mis.sig.getName().equals(Method.CONSTRUCTOR_METHOD_NAME)) {
                    constructors.add(translateMethod(mis, false, -1));
                } else {
                    instanceMethods.add(mis);
                }
            }
        }

        instanceMethods.sort(PRIVATE_LAST_COMPARATOR);
        Method[] parentMethods = parent != null && !isInterface ? parent.getInstanceMethods() : null;
        List<Method> orderedMethods = new ArrayList<>();

        Set<Method> retainedParentMethods = new HashSet<>();

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
                        orderedMethods.add(translateMethod(mis, false, parentMethod.getOffset()));
                        li.remove();
                        overridden = true;
                        break;
                    }

                    if (mis.mi.hasModifier(Modifier.PRIVATE)) {
                        // we known nothing after this point can be overriding
                        break;
                    }
                }
                if (!overridden) {
                    // copy down the superClass's Method
                    retainedParentMethods.add(parentMethod);
                    orderedMethods.add(parentMethod);
                }
            }
        }

        int instanceOffset = orderedMethods.size();
        for (MethodInfoAndSig instanceMethod : instanceMethods) {
            orderedMethods.add(translateMethod(instanceMethod, isInterface, instanceOffset++));
        }

        // Fields
        Field[] translatedStaticFields;
        Field[] translatedInstanceFields;


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

        if (isInterface) {
            if (!instanceFields.isEmpty()) {
                throw new IllegalStateException("Should not have instance fields on interface " + className);
            }
            translatedInstanceFields = new Field[0];
        } else {
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
            constructors.toArray(new Method[constructors.size()]),
            staticMethods.toArray(new Method[staticMethods.size()]),
            translatedInstanceFields,
            translatedStaticFields,
            this);

        if (className.equals("java/lang/Thread")) {
            registerThreadCreationHookForThreadInit(co);
        }

        if (!isInterface) {
            registerInterfaceImplementations(co);
        }

        cacheFields(co.getInstanceFields(), className);
        cacheFields(co.getStaticFields(), className);

        cacheMethods(co.getInstanceMethods(), className, retainedParentMethods);
        cacheMethods(co.getConstructors(), className, emptySet());
        cacheMethods(co.getStaticMethods(), className, emptySet());

        translateSimpleConstantPool(file.getConstantPool());

        return co;
    }

    /**
     * We need to tap in to the Thread.init() method in order to actually create a native thread ourselves
     */
    private static void registerThreadCreationHookForThreadInit(ClassObject threadClass) {
        Method[] threadMethods = threadClass.getInstanceMethods();
        for (int i = 0; i < threadMethods.length; i++) {
            Method method = threadMethods[i];
            MethodSignature ms = method.getSignature();
            if (ms.getName().equals("init") && ms.getArgTypes().size() == 5) {
                method.getModifiers().add(Modifier.NATIVE);
                NativeImplementation ni = new NativeImplementation() {
                    @Override
                    public NativeReturn execute(Variables args, OperationContext ctx) {
                        OopClass thread = args.getOop(0);
                        OopClass name = args.getOop(3);
                        Threads.register(new Thread(thread, name));

                        // This method is a special case where our hijacking native implementation calls an underlying
                        // real implementation (in the Java source code).
                        // We need to remove the stack frame corresponding to our native call, otherwise there will be
                        // duplicate frames, which screws up GC.
                        ctx.getExecutionStack().pop();

                        // Call the real method
                        Utils.executeMethodAndThrow(method, args, ctx.getThread());

                        return NativeReturn.forVoid();
                    }

                    @Override
                    public String getClassName() {
                        return "java/lang/Thread";
                    }

                    @Override
                    public MethodSignature getMethodSignature() {
                        return ms;
                    }
                };

                Set<Modifier> modifiers = EnumSet.copyOf(method.getModifiers());
                modifiers.add(Modifier.NATIVE);
                threadMethods[i] = new NativeMethod(modifiers, method.getAttributes(), ms, method.getOffset(), ni);
                threadMethods[i].setClassObject(method.getClassObject());
                return;
            }
        }
    }

    private static void registerInterfaceImplementations(ClassObject co) {
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

    private static void addInterfaces(ClassObject classObject, Set<ClassObject> interfaces) {
        for (ClassObject iface : classObject.getInterfaces()) {
            interfaces.add(iface);
            addInterfaces(iface, interfaces);
        }
    }

    private static void executeStaticInitMethod(ClassObject co) {
        Method staticInit = co.findMethod("<clinit>", "()V", true);
        if (staticInit != null) {
            Utils.executeMethodAndThrow(staticInit, new Variables(staticInit.getCode().getMaxLocals()), Utils.currentThread());
        }
    }

    private static Method translateMethod(MethodInfoAndSig mis, boolean isInterface, int offset) {
        MemberInfo info = mis.mi;

        if (isInterface) {
            return new InterfaceMethod(info.getModifiers(), info.getAttributes(), mis.sig);
        } else {
            if (info.hasModifier(Modifier.NATIVE)) {
                NativeImplementation nativeImplementation = NativeImplementationRegistry.getNativeExecution(mis.className, mis.sig);
                if (nativeImplementation == null) {
                    // This will only be an issue if we actually try to use this method, in which case an exception is thrown
                    LOG.info("Native method missing: {}.{}", mis.className, mis.sig);
                }
                return new NativeMethod(info.getModifiers(), info.getAttributes(), mis.sig, offset, nativeImplementation);
            } else {
                return new Method(info.getModifiers(), info.getAttributes(), mis.sig, offset);
            }
        }
    }

    private void cacheMethods(Method[] methods, String className, Set<Method> skipMethods) {
        for (Method method : methods) {
            if (!skipMethods.contains(method)) {
                MethodKey key = new MethodKey(className, method.getSignature());
                this.methods.put(key, method);
            }
        }
    }

    private void cacheFields(Field[] fields, String className) {
        for (Field field : fields) {
            FieldKey key = new FieldKey(className, field.getName(), field.getType());
            this.fields.put(key, field);
        }
    }


    private static void translateSimpleConstantPool(Object[] constantPool) {
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
            offset = lastField.getOffset() + lastField.getType().getWidth();
        }

        for (MemberInfo field : fomis) {
            Type type = Types.parseType((String) constantPool[field.getDescriptorIndex()]);
            String name = (String) constantPool[field.getNameIndex()];
            fields.add(new Field(field.getModifiers(), field.getAttributes(), name, type, offset));
            offset += type.getWidth();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    private static String getClassName(int index, Object[] constantPool) {
        CpClass cpClass = (CpClass) constantPool[index];
        return (String) constantPool[cpClass.getNameIndex()];
    }

    public AbstractClassObject translate(CpClass cpClass, Object[] constantPool) {
        String className = (String) constantPool[cpClass.getNameIndex()];
        return className.startsWith("[") ?
            ArrayClassObject.forType((ArrayType) Types.parseType(className)) :
            getClassObject(className);
    }

    public Field translate(CpFieldReference cfr, Object[] constantPool) {
        ClassObject co = findClassObject(cfr, constantPool);
        final ClassObject coCopy = co;

        NameAndType nat = (NameAndType) constantPool[cfr.getNameAndTypeIndex()];
        String name = (String) constantPool[nat.getNameIndex()];
        Type type = Types.parseType((String) constantPool[nat.getDescriptorIndex()]);

        while (co != null) {
            FieldKey key = new FieldKey(co.getClassName(), name, type);
            Field field = fields.get(key);

            if (field != null) {
                return field;
            }
            co = co.getSuperClass();
        }

        throw new IllegalArgumentException("No Field for " + new FieldKey(coCopy.getClassName(), name, type));
    }

    public Method translate(CpMethodReference cmr, Object[] constantPool) {
        ClassObject co = findClassObject(cmr, constantPool);
        final ClassObject coCopy = co;

        NameAndType nat = (NameAndType) constantPool[cmr.getNameAndTypeIndex()];
        String name = (String) constantPool[nat.getNameIndex()];
        String descriptor = (String) constantPool[nat.getDescriptorIndex()];

        MethodSignature methodSignature = MethodSignature.parse(name, descriptor);

        Method method;
        if (co.hasModifier(Modifier.INTERFACE)) {
            method = translateInterfaceMethod(co, methodSignature);
        } else {
            do {
                method = methods.get(new MethodKey(co.getClassName(), methodSignature));
                co = co.getSuperClass();
            } while (method == null && co != null);
        }

        if (method == null) {
            throw new IllegalArgumentException("No Method for " + new MethodKey(coCopy.getClassName(), methodSignature));
        }

        return method;
    }

    private Method translateInterfaceMethod(ClassObject ifaceCo, MethodSignature methodSignature) {
        Method method = methods.get(new MethodKey(ifaceCo.getClassName(), methodSignature));

        if (method != null) {
            return method;
        }

        for (ClassObject iface : ifaceCo.getInterfaces()) {
            method = translateInterfaceMethod(iface, methodSignature);
            if (method != null) {
                return method;
            }
        }
        return null;
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
        return BOOTSTRAP_CLASSLOADER;
    }

}
