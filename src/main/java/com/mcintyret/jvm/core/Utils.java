package com.mcintyret.jvm.core;

import java.util.concurrent.atomic.AtomicInteger;

import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.domain.ArrayType;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.thread.Thread;

public class Utils {

    public static OopArray newArray(Type type, int size) {
        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(type, 1));
        return new OopArray(aco, new int[size * type.getSimpleType().getWidth()]);
    }

    public static long toLong(int l, int r) {
        return (long) l << 32 | r & 0xFFFFFFFFL;
    }

    public static String toString(OopClass stringOop) {
        return toString((OopArray) Heap.getOop(stringOop.getFields()[0]));
    }

    public static String toString(OopArray charArrayOop) {
        char[] chars = new char[charArrayOop.getLength()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) charArrayOop.getFields()[i];
        }
        return new String(chars);
    }

    public static String toString(int oopAddress) {
        Oop oop = Heap.getOop(oopAddress);
        if (oop instanceof OopArray) {
            return toString((OopArray) oop);
        } else {
            return toString((OopClass) oop);
        }
    }

    public static NativeReturn executeMethodAndThrow(Method method, int[] args, Thread thread) {
        ExecutionStack stack = new ExecutionStack(thread);

        stack.push(new ExecutionStackElement(method, args,
            method.getClassObject().getConstantPool(), stack));

        stack.execute();

        // TODO: make less shit
        NativeReturn ret = stack.getFinalReturn();
        String message;
        if (ret.isThrowable()) {
            AtomicInteger address = new AtomicInteger();
            ret.applyValue(new ValueReceiver() {
                @Override
                public void receiveInt(int i) {
                    address.set(i);
                }

                @Override
                public void receiveLong(long l) {
                    throw new UnsupportedOperationException();
                }
            });
            OopClass thrown = Heap.getOopClass(address.get());
            message = Utils.toString((OopClass) thrown.getClassObject().findField("detailMessage", false).getOop(thrown));

            // TODO: proper exception type
            throw new RuntimeException("Error executing method " + method + ": " + thrown.getClassObject() + "(" + message + ")");
        }
        return ret;
    }


}

