package com.mcintyret.jvm.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class MethodSignature {

    private final String name;

    private final List<Type> argTypes;

    private final Type returnType;

    public static MethodSignature parse(String name, String description) {
        CharIterator it = new CharIterator(description);
        if (it.next() != '(') {
            throw new IllegalArgumentException("Invalid description: " + description);
        }
        List<Type> argTypes = new ArrayList<>(4);
        while (it.peek() != ')') {
            argTypes.add(parseType(it));
        }
        it.next(); // get rid of the ')'
        argTypes = argTypes.isEmpty() ? Collections.emptyList() : argTypes;

        Type returnType = parseType(it);

        return new MethodSignature(name, argTypes, returnType);
    }

    private static Type parseType(CharIterator it) {
        int arrayDimensions = 0;
        char[] classBuffer = null;
        int classIndex = 0;
        while (it.hasNext()) {
            char c = it.next();
            if (classBuffer == null) {
                switch (c) {
                    case 'I':
                        return toType(SimpleType.INTEGER, arrayDimensions);
                    case 'J':
                        return toType(SimpleType.LONG, arrayDimensions);
                    case 'Z':
                        return toType(SimpleType.BOOLEAN, arrayDimensions);
                    case 'B':
                        return toType(SimpleType.BYTE, arrayDimensions);
                    case 'C':
                        return toType(SimpleType.CHAR, arrayDimensions);
                    case 'F':
                        return toType(SimpleType.FLOAT, arrayDimensions);
                    case 'D':
                        return toType(SimpleType.DOUBLE, arrayDimensions);
                    case 'L':
                        // TODO: is there an actual maximum to this? Or a better way to do it?
                        classBuffer = new char[1000];
                }
            } else {
                if (c == ';') {
                    return ReferenceType.forClass(new String(classBuffer));
                } else {
                    classBuffer[classIndex++] = c;
                }
            }
        }
        throw new IllegalStateException();
    }

    private static Type toType(Type type, int arrayDimensions) {
        return arrayDimensions == 0 ? type : ArrayType.create(type, arrayDimensions);
    }

    public MethodSignature(String name, List<Type> argTypes, Type returnType) {
        this.name = name;
        this.argTypes = argTypes;
        this.returnType = returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodSignature that = (MethodSignature) o;

        if (!argTypes.equals(that.argTypes)) return false;
        if (!name.equals(that.name)) return false;
        if (!returnType.equals(that.returnType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + argTypes.hashCode();
        result = 31 * result + returnType.hashCode();
        return result;
    }

    public String getName() {

        return name;
    }

    public List<Type> getArgTypes() {
        return argTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    private static class CharIterator {

        private final CharSequence seq;

        int i = 0;

        private CharIterator(CharSequence seq) {
            this.seq = seq;
        }

        char next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return seq.charAt(i++);
        }

        char peek() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return seq.charAt(i);
        }

        boolean hasNext() {
            return i != seq.length() - 1;
        }
    }
}
