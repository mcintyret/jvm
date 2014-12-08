package com.mcintyret.jvm.core.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodSignature {

    private final String name;

    private final List<Type> argTypes;

    private final Type returnType;

    private final int totalWidth;

    public static MethodSignature parse(String name, String descriptor) {
        CharIterator it = new CharIterator(descriptor);
        if (it.next() != '(') {
            throw new IllegalArgumentException("Invalid descriptor: " + descriptor);
        }
        List<Type> argTypes = new ArrayList<>(4);
        while (it.peek() != ')') {
            argTypes.add(Types.parseType(it));
        }
        it.next(); // get rid of the ')'
        argTypes = argTypes.isEmpty() ? Collections.emptyList() : argTypes;

        Type returnType = Types.parseType(it);

        return new MethodSignature(name, argTypes, returnType);
    }


    public MethodSignature(String name, List<Type> argTypes, Type returnType) {
        this.name = name;
        this.argTypes = argTypes;
        this.returnType = returnType;
        this.totalWidth = argTypes.stream().mapToInt(Type::getWidth).sum();
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

    public int getTotalWidth() {
        return totalWidth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(":(");

        for (Type argType : argTypes) {
            sb.append(argType);
        }
        return sb.append(')').append(returnType).toString();
    }

}
