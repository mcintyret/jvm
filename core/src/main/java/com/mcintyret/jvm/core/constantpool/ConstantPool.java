package com.mcintyret.jvm.core.constantpool;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.AbstractClassObject;
import com.mcintyret.jvm.core.clazz.Field;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.exec.WideVariable;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.load.ClassLoader;
import com.mcintyret.jvm.parse.cp.CpClass;
import com.mcintyret.jvm.parse.cp.CpFieldReference;
import com.mcintyret.jvm.parse.cp.CpMethodReference;
import com.mcintyret.jvm.parse.cp.CpString;

// TODO: this all needs to be typed!
public class ConstantPool {

    private final Object[] constantPool;

    private final ClassLoader loader;

    public ConstantPool(Object[] constantPool, com.mcintyret.jvm.load.ClassLoader loader) {
        this.constantPool = constantPool;
        this.loader = loader;
    }

    public AbstractClassObject getClassObject(int i) {
        if (constantPool[i] instanceof AbstractClassObject) {
            return (AbstractClassObject) constantPool[i];
        }

        return translateClassObject(i);
    }

    private AbstractClassObject translateClassObject(int i) {
        AbstractClassObject obj = loader.translate((CpClass) constantPool[i], constantPool);
        constantPool[i] = obj;
        return obj;
    }

    public Field getField(int i) {
        if (constantPool[i] instanceof Field) {
            return (Field) constantPool[i];
        }

        Field ref = loader.translate((CpFieldReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    public Method getMethod(int i) {
        if (constantPool[i] instanceof Method) {
            return (Method) constantPool[i];
        }

        Method ref = loader.translate((CpMethodReference) constantPool[i], constantPool);
        constantPool[i] = ref;
        return ref;
    }

    public void getConstant(int i, ValueReceiver receiver) {
        Object constant = constantPool[i];
        if (constant instanceof Variable) {
            Variable v = (Variable) constant;
            receiver.receiveSingleWidth(v.getValue(), v.getType());
        } else if (constant instanceof WideVariable) {
            WideVariable v = (WideVariable) constant;
            receiver.receiveDoubleWidth(v.getValue(), v.getType());
        } else if (constant instanceof AbstractClassObject) {
            receiver.receiveSingleWidth(((AbstractClassObject) constant).getOop().getAddress(), SimpleType.REF);
        } else {
            switch (constant.getClass().getSimpleName().toLowerCase()) {
                case "boolean":
                    constantPool[i] = new Variable(SimpleType.BOOLEAN, Utils.toInt((Boolean) constant));
                    break;
                case "byte":
                    constantPool[i] = new Variable(SimpleType.BYTE, (Byte) constant);
                    break;
                case "short":
                    constantPool[i] = new Variable(SimpleType.SHORT, (Short) constant);
                    break;
                case "character":
                    constantPool[i] = new Variable(SimpleType.CHAR, (Character) constant);
                    break;
                case "integer":
                    constantPool[i] = new Variable(SimpleType.INT, (Integer) constant);
                    break;
                case "long":
                    constantPool[i] = new WideVariable(SimpleType.LONG, (Long) constant);
                    break;
                case "float":
                    constantPool[i] = new Variable(SimpleType.FLOAT, Utils.toInt((Float) constant));
                    break;
                case "double":
                    constantPool[i] = new WideVariable(SimpleType.DOUBLE, Utils.toLong((Double) constant));
                    break;
                default:
                    if (constant instanceof CpString) {
                        int index = ((CpString) constant).getStringIndex();
                        String string = (String) constantPool[index];
                        constantPool[i] = new Variable(SimpleType.REF, Heap.intern(string));
                    } else if (constantPool[i] instanceof CpClass) {
                        constantPool[i] = translateClassObject(i);
                    } else {
                        throw new AssertionError("Can i get here??"); // TODO: less silly!
                    }
            }
            getConstant(i, receiver);
        }
    }
}
