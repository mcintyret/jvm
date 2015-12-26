package com.mcintyret.jvm.core.util;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ArrayClassObject;
import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.clazz.Method;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.exec.Thread;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.nativeimpls.NativeReturn;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.oop.OopArray;
import com.mcintyret.jvm.core.oop.OopClass;
import com.mcintyret.jvm.core.type.ArrayType;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.load.Runner;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.mcintyret.jvm.load.ClassLoader.getDefaultClassLoader;

public class Utils {

    public static OopArray newArray(Type type, int size) {
        ArrayClassObject aco = ArrayClassObject.forType(ArrayType.create(type, 1));
        return new OopArray(aco, new Variables(size * type.getWidth()));
    }

    public static long toLong(double d) {
        return Double.doubleToLongBits(d);
    }

    public static long toLong(int l, int r) {
        return (long) l << 32 | r & 0xFFFFFFFFL;
    }

    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }

    public static int toInt(float f) {
        return Float.floatToIntBits(f);
    }

    public static String toString(OopClass stringOop) {
        if (stringOop == null) {
            return null;
        }
        return toString((OopArray) stringOop.getFields().getOop(0));
    }

    public static String toString(OopArray charArrayOop) {
        if (charArrayOop == null) {
            return null;
        }
        char[] chars = new char[charArrayOop.getLength()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) charArrayOop.getFields().getRawValue(i);
        }
        return new String(chars);
    }

    public static String toString(Oop oop) {
        if (oop instanceof OopArray) {
            return toString((OopArray) oop);
        } else {
            return toString((OopClass) oop);
        }
    }

    public static String toString(int oopAddress) {
        return toString(Heap.getOop(oopAddress));
    }

    public static OopClass toOopString(String string) {
        if (string == null) {
            return null;
        }

        ClassObject stringClass = getDefaultClassLoader().getClassObject("java/lang/String");
        OopClass stringOop = stringClass.newObject();
        Heap.allocate(stringOop);

        Variables chars = new Variables(string.length());
        for (int i = 0; i < string.length(); i++) {
            chars.put(i, SimpleType.CHAR, string.charAt(i));
        }
        ArrayClassObject co = ArrayClassObject.forType(ArrayType.create(SimpleType.CHAR, 1));
        OopArray charArrayOop = new OopArray(co, chars);

        stringOop.getFields().put(0, SimpleType.REF, Heap.allocate(charArrayOop));
        // The only other field, hash, is initially 0 and so doesn't need changing

        return stringOop;
    }

    public static NativeReturn executeMethodAndThrow(Method method, Variables args, Thread thread) {
        // TODO: make less shit
        NativeReturn ret = thread.execute(method, args);
        String message;
        if (ret.isThrowable()) {
            AtomicInteger address = new AtomicInteger();
            ret.applyValue(new ValueReceiver() {

                @Override
                public void receiveSingleWidth(int i, SimpleType type) {
                    address.set(i);
                }

                @Override
                public void receiveDoubleWidth(long l, SimpleType type) {
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

    public static OopClass construct(ClassObject co, Thread thread) {
        return construct(co, "()V", thread);
    }

    public static OopClass construct(ClassObject co, String ctorSignature, Thread thread, Oop... args) {
        OopClass obj = Heap.allocateAndGet(co.newObject());
        Method ctor = co.findConstructor(ctorSignature);

        Oop[] allArgs = new Oop[args.length + 1];
        allArgs[0] = obj;
        if (args.length > 0) {
            System.arraycopy(args, 0, allArgs, 1, args.length);
        }

        Utils.executeMethodAndThrow(ctor, ctor.newArgArray(allArgs), thread);

        return obj;
    }

    // TODO: proper stack-traces for Throwables thrown from native code!
    public static OopClass toThrowableOop(Throwable t, Thread thread) {
        ClassObject co = getDefaultClassLoader().getClassObject(toJvmClassName(t.getClass()));

        OopClass messageOop = toOopString(t.getMessage());

        Throwable cause = t.getCause();
        String ctorSig;
        Oop[] args;
        if (cause == null) {
            ctorSig = "(Ljava/lang/String;)V";
            args = new Oop[]{messageOop};
        } else {
            ctorSig = "(Ljava/lang/String;Ljava/lang/Throwable;)V";
            args = new Oop[]{messageOop, toThrowableOop(cause, thread)};
        }
        return construct(co, ctorSig, thread, args);
    }

    public static String toJvmClassName(Class<?> clazz) {
        return toJvmClassName(clazz.getName());
    }

    public static String toJvmClassName(String className) {
        return className.replaceAll("\\.", "/");
    }

    public static ClassObject getClassObject(String className) {
        return getDefaultClassLoader().getClassObject(className);
    }

    public static Thread currentThread() {
        java.lang.Thread javaThread = java.lang.Thread.currentThread();
        if (javaThread instanceof Thread.ActualThread) {
            return ((Thread.ActualThread) javaThread).getThread();
        } else if (javaThread.getName().equals("main")) {
            return Runner.MAIN_THREAD;
        } else {
            throw new IllegalStateException("Unknown current thread: " + javaThread);
        }
    }

    public static <S, T> T[] transformArray(S[] source, Function<S, T> func, Class<T> targetClass) {
        T[] target = (T[]) Array.newInstance(targetClass, source.length);
        for (int i = 0; i < source.length; i++) {
            target[i] = func.apply(source[i]) ;
        }
        return target;
    }


}

