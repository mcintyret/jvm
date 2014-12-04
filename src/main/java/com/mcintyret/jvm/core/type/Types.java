package com.mcintyret.jvm.core.type;

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
                if (c == '[') {
                    arrayDimensions++;
                } else {
                    SimpleType simpleType = SimpleType.forChar(c);
                    if (simpleType == SimpleType.REF) {
                        // TODO: is there an actual maximum to this? Or a better way to do it?
                        classBuffer = new char[1000];
                    } else if (simpleType != null) {
                        return toType(simpleType, arrayDimensions);
                    }
                }
            } else {
                if (c == ';') {
                    return toType(NonArrayType.forClass(new String(classBuffer, 0, classIndex)), arrayDimensions);
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
