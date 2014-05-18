package com.mcintyret.jvm.core.domain;

public class Types {

    public static Type parseType(String str) {
        return parseType(new CharIterator(str));
    }

    static Type parseType(CharIterator it) {
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

}
