package com.mcintyret.jvm.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static Object executeMethod(Method m, Object... args) {
        try {
            return m.invoke(null, args);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static Method findMethod(Class<?> clazz, String name, boolean isStatic) {
        Method found = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) != isStatic) {
                continue;
            }
            if (m.getName().equals(name)) {
                if (found != null) {
                    throw new IllegalArgumentException("Multiple methods named " + name + " in " + clazz);
                }
                found = m;
            }
        }
        if (found == null) {
            throw new IllegalStateException("No method named " + name + " in " + clazz);
        }

        found.setAccessible(true);
        return found;
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (ReflectiveOperationException e) {
          throw new AssertionError(e);
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Class<?> clazz = obj.getClass();
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (ReflectiveOperationException e) {
          throw new AssertionError(e);
        }
    }

}
