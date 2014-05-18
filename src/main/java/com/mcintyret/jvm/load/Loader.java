//package com.mcintyret.jvm.load;
//
//import com.mcintyret.jvm.core.ByteCode;
//import com.mcintyret.jvm.core.ClassObject;
//import com.mcintyret.jvm.core.Method;
//import com.mcintyret.jvm.core.domain.MethodSignature;
//import com.mcintyret.jvm.parse.ClassFile;
//import com.mcintyret.jvm.parse.ClassFileReader;
//import com.mcintyret.jvm.parse.FieldOrMethodInfo;
//import com.mcintyret.jvm.parse.Modifier;
//import com.mcintyret.jvm.parse.attribute.Attribute;
//import com.mcintyret.jvm.parse.attribute.AttributeType;
//import com.mcintyret.jvm.parse.attribute.Code;
//import com.mcintyret.jvm.parse.cp.CpClass;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//
//public class Loader {
//
//    public static void load(Iterable<InputStream> classFiles) throws IOException {
//        ClassFileReader reader = new ClassFileReader();
//
//        Map<String, ClassFile> loadedClassFiles = new HashMap<>();
//
//        for (InputStream stream : classFiles) {
//            ClassFile file = reader.read(stream);
//
//            loadedClassFiles.put(getClassName(file.getThisClass(), file), file);
//        }
//
//        Map<String, ClassObject> loadedClasses = new HashMap<>();
//        while (!loadedClassFiles.isEmpty()) {
//            List<String> classNames = new ArrayList<>(loadedClassFiles.keySet());
//
//            for (String className : classNames) {
//                ClassFile file = loadedClassFiles.remove(className);
//                ClassObject parent;
//                int superClass = file.getSuperClass();
//                if (superClass == 0) {
//                    parent = null;
//                } else if ((parent = loadedClasses.get(getClassName(superClass, file))) == null) {
//                    loadedClassFiles.put(className, file);
//                } else {
////                    loadedClasses.put(className,
////                        new ClassObject(
////                            parent,
////                            new ConstantPool(file.getConstantPool()),
////
////                        ))
//                }
//            }
//        }
//    }
//
//    private static ClassObject makeClassObject(ClassFile file, ClassObject parent) {
//        // Methods - sorting out the VTable
//        List<Method> staticMethods = new ArrayList<>();
//        List<FomiAndMethodSig> instanceMethods = new LinkedList<>();
//
//        for (FieldOrMethodInfo method : file.getMethods()) {
//            if (method.hasModifier(Modifier.STATIC)) {
//                staticMethods.add(translate(new FomiAndMethodSig(method, file.getConstantPool())));
//            } else {
//                instanceMethods.add(new FomiAndMethodSig(method, file.getConstantPool()));
//            }
//        }
//
//        instanceMethods.sort(PRIVATE_LAST_COMPARATOR);
//        Method[] parentMethods = parent != null ? parent.getInstanceMethods() : null;
//        List<Method> orderedMethods = new ArrayList<>();
//
//        if (parentMethods != null) {
//            for (Method parentmethod : parentMethods) {
//                if (parentmethod.hasModifier(Modifier.PRIVATE)) {
//                    // we known nothing after this point can be overridden
//                    break;
//                }
//                ListIterator<FomiAndMethodSig> li = instanceMethods.listIterator();
//                boolean overridden = false;
//                while (li.hasNext()) {
//                    FomiAndMethodSig fms = li.next();
//                    if (fms.sig.equals(parentmethod.getMethodSignature())) {
//                        // Override!!
//                        orderedMethods.add(translate(fms));
//                        li.remove();
//                        overridden = true;
//                        break;
//                    }
//                }
//                if (!overridden) {
//                    // copy down the parent's Method
//                    orderedMethods.add(parentmethod);
//                }
//            }
//        }
//
//        for (FomiAndMethodSig instanceMethod : instanceMethods) {
//            orderedMethods.add(translate(instanceMethod));
//        }
//
//        // Fields
//        List<FieldOrMethodInfo> staticFields = new ArrayList<>();
//        List<FieldOrMethodInfo> instanceFields = new ArrayList<>();
//        for (FieldOrMethodInfo field : file.getFields()) {
//            if (field.hasModifier(Modifier.STATIC)) {
//                staticFields.add(field);
//            } else {
//                instanceFields.add(field);
//            }
//        }
//
//
//
//        return null;
//    }
//
//    private static Method translate(FomiAndMethodSig fms) {
//        FieldOrMethodInfo info = fms.fomi;
//        byte[] code = null;
//
//        for (Attribute a : info.getAttributes()) {
//            if (a.getType() == AttributeType.CODE) {
//                code = ((Code) a).getCode();
//                break;
//            }
//        }
//        // code could still be null if this is an ABSTRACT or NATIVE method
//        return new Method(new ByteCode(code), fms.sig, info.getModifiers());
//    }
//
//
//    private static String getClassName(int index, ClassFile file) {
//        CpClass cpClass = (CpClass) file.getConstantPool()[index];
//        return (String) file.getConstantPool()[cpClass.getNameIndex()];
//    }
//
//    private static final Comparator<FieldOrMethodInfo> PRIVATE_LAST_COMPARATOR = (a, b) -> {
//        if (a.hasModifier(Modifier.PRIVATE)) {
//            return b.hasModifier(Modifier.PRIVATE) ? 0 : -1;
//        } else {
//            return !b.hasModifier(Modifier.PRIVATE) ? 0 : 1;
//        }
//    };
//
//    private static class FomiAndMethodSig {
//
//        private final FieldOrMethodInfo fomi;
//
//        private final MethodSignature sig;
//
//        private FomiAndMethodSig(FieldOrMethodInfo fomi, Object[] constantPool) {
//            this.fomi = fomi;
//            String name = (String) constantPool[fomi.getNameIndex()];
//            String descriptor = (String) constantPool[fomi.getDescriptorIndex()];
//            sig = MethodSignature.parse(name, descriptor);
//        }
//    }
//
//}
